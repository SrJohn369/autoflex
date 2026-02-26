import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';
import { loadProductById, saveProduct, clearCurrent } from '../store/slices/productSlice';
import { loadRawMaterials as loadRawMaterialsList } from '../store/slices/rawMaterialSlice';
import Modal from '../components/Modal';

export default function ProductForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isNew = !id || id === 'new';
  const { current, error } = useSelector((s) => s.products);
  const { list: rawMaterialsList } = useSelector((s) => s.rawMaterials);

  const [code, setCode] = useState('');
  const [name, setName] = useState('');
  const [value, setValue] = useState('');
  const [materials, setMaterials] = useState([]);

  const [confirmModal, setConfirmModal] = useState({ isOpen: false });
  const [feedbackModal, setFeedbackModal] = useState({ isOpen: false, type: 'success', title: '', message: '' });

  useEffect(() => {
    dispatch(loadRawMaterialsList());
  }, [dispatch]);

  useEffect(() => {
    if (!isNew && id) {
      dispatch(loadProductById(id));
    } else {
      dispatch(clearCurrent());
      setCode(localStorage.getItem('lastProductCode') || '');
      setName('');
      setValue('');
      setMaterials([]);
    }
  }, [id, isNew, dispatch]);

  useEffect(() => {
    if (current && !isNew) {
      setCode(current.code ?? '');
      setName(current.name ?? '');
      setValue(current.value ?? '');
      setMaterials(current.materials?.length ? current.materials.map((m) => ({
        rawMaterialId: m.rawMaterialId,
        rawMaterialCode: m.rawMaterialCode,
        rawMaterialName: m.rawMaterialName,
        quantityRequired: String(m.quantityRequired ?? ''),
      })) : []);
    }
  }, [current, isNew]);

  const addMaterial = () => {
    setMaterials((prev) => [...prev, { rawMaterialId: '', rawMaterialCode: '', rawMaterialName: '', quantityRequired: '' }]);
  };

  const removeMaterial = (index) => {
    setMaterials((prev) => prev.filter((_, i) => i !== index));
  };

  const updateMaterial = (index, field, val) => {
    setMaterials((prev) => {
      const next = [...prev];
      if (field === 'rawMaterialId') {
        const rm = rawMaterialsList.find((r) => r.id === Number(val));
        next[index] = {
          ...next[index],
          rawMaterialId: val ? Number(val) : '',
          rawMaterialCode: rm?.code ?? '',
          rawMaterialName: rm?.name ?? '',
        };
      } else {
        next[index] = { ...next[index], [field]: val };
      }
      return next;
    });
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    setConfirmModal({ isOpen: true });
  };

  const executeSave = () => {
    setConfirmModal({ isOpen: false });
    const payload = {
      code: code.trim(),
      name: name.trim(),
      value: value === '' ? 0 : Number(value),
      materials: materials
        .filter((m) => m.rawMaterialId && m.quantityRequired)
        .map((m) => ({
          rawMaterialId: Number(m.rawMaterialId),
          quantityRequired: Number(m.quantityRequired),
        })),
    };

    dispatch(saveProduct({ id: isNew ? null : Number(id), product: payload }))
      .then((result) => {
        if (saveProduct.fulfilled.match(result)) {
          if (isNew) {
            localStorage.setItem('lastProductCode', payload.code);
          }
          setFeedbackModal({
            isOpen: true,
            type: 'success',
            title: 'Sucesso',
            message: isNew ? 'Adicionado com sucesso' : 'Editado com sucesso',
          });
        } else {
          setFeedbackModal({
            isOpen: true,
            type: 'error',
            title: 'Erro ao salvar',
            message: result.error?.message || 'Ocorreu um erro inesperado.',
          });
        }
      });
  };

  const closeFeedback = () => {
    if (feedbackModal.type === 'success') {
      navigate('/');
    }
    setFeedbackModal({ isOpen: false, type: 'success', title: '', message: '' });
  };

  return (
    <div className="page">
      <div className="page-header">
        <h2>{isNew ? 'Novo produto' : 'Editar produto'}</h2>
        <button type="button" className="btn btn-secondary" onClick={() => navigate('/')}>Voltar</button>
      </div>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleFormSubmit} className="form">
        <div className="form-group">
          <label htmlFor="code">Código</label>
          <input id="code" value={code} onChange={(e) => setCode(e.target.value)} required />
        </div>
        <div className="form-group">
          <label htmlFor="name">Nome</label>
          <input id="name" value={name} onChange={(e) => setName(e.target.value)} required />
        </div>
        <div className="form-group">
          <label htmlFor="value">Valor R$</label>
          <input id="value" type="number" step="0.01" min="0" value={value} onChange={(e) => setValue(e.target.value)} required />
        </div>

        <div className="form-section">
          <h3>Matéria-prima (quantidade requerida por unidade)</h3>
          {materials.map((m, idx) => (
            <div key={idx} className="form-row inline">
              <select
                value={m.rawMaterialId || ''}
                onChange={(e) => updateMaterial(idx, 'rawMaterialId', e.target.value)}
                required
              >
                <option value="">Selecionar Material</option>
                {rawMaterialsList.map((rm) => (
                  <option key={rm.id} value={rm.id}>{rm.code} – {rm.name}</option>
                ))}
              </select>
              <input
                type="number"
                step="0.0001"
                min="0.0001"
                placeholder="Quantidade requerida"
                value={m.quantityRequired}
                onChange={(e) => updateMaterial(idx, 'quantityRequired', e.target.value)}
              />
              <button type="button" className="btn btn-sm btn-danger" onClick={() => removeMaterial(idx)}>Remover</button>
            </div>
          ))}
          <button type="button" className="btn btn-secondary" onClick={addMaterial}>Adicionar Material</button>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-primary">Salvar</button>
          <button type="button" className="btn btn-secondary" onClick={() => navigate('/')}>Cancelar</button>
        </div>
      </form>

      <Modal
        isOpen={confirmModal.isOpen}
        title="Confirmar Salvamento"
        message="Deseja realmente salvar essas alterações?"
        type="info"
        onConfirm={executeSave}
        onCancel={() => setConfirmModal({ isOpen: false })}
        confirmText="Salvar"
      />

      <Modal
        isOpen={feedbackModal.isOpen}
        title={feedbackModal.title}
        message={feedbackModal.message}
        type={feedbackModal.type}
        onConfirm={closeFeedback}
        confirmText="OK"
      />
    </div>
  );
}
