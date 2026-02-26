import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';
import { loadRawMaterialById, saveRawMaterial, clearCurrent } from '../store/slices/rawMaterialSlice';
import Modal from '../components/Modal';

export default function RawMaterialForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isNew = !id || id === 'new';
  const { current, error } = useSelector((s) => s.rawMaterials);

  const [code, setCode] = useState('');
  const [name, setName] = useState('');
  const [quantityInStock, setQuantityInStock] = useState('');

  // Modal states
  const [confirmModal, setConfirmModal] = useState({ isOpen: false });
  const [feedbackModal, setFeedbackModal] = useState({ isOpen: false, type: 'success', title: '', message: '' });

  useEffect(() => {
    if (!isNew && id) {
      dispatch(loadRawMaterialById(id));
    } else {
      dispatch(clearCurrent());
      setCode(localStorage.getItem('lastRawMaterialCode') || '');
      setName('');
      setQuantityInStock('');
    }
  }, [id, isNew, dispatch]);

  useEffect(() => {
    if (current && !isNew) {
      setCode(current.code ?? '');
      setName(current.name ?? '');
      setQuantityInStock(current.quantityInStock ?? '');
    }
  }, [current, isNew]);

  const handleFormSubmit = (e) => {
    e.preventDefault();
    setConfirmModal({ isOpen: true });
  };

  const executeSave = () => {
    setConfirmModal({ isOpen: false });
    const payload = {
      code: code.trim(),
      name: name.trim(),
      quantityInStock: quantityInStock === '' ? 0 : Number(quantityInStock),
    };

    dispatch(saveRawMaterial({ id: isNew ? null : Number(id), rawMaterial: payload }))
      .then((result) => {
        if (saveRawMaterial.fulfilled.match(result)) {
          if (isNew) {
            localStorage.setItem('lastRawMaterialCode', payload.code);
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
      navigate('/raw-materials');
    }
    setFeedbackModal({ isOpen: false, type: 'success', title: '', message: '' });
  };

  return (
    <div className="page">
      <div className="page-header">
        <h2>{isNew ? 'Nova matéria-prima' : 'Editar matéria-prima'}</h2>
        <button type="button" className="btn btn-secondary" onClick={() => navigate('/raw-materials')}>Voltar</button>
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
          <label htmlFor="qty">Quantidade em estoque</label>
          <input id="qty" type="number" step="0.0001" min="0" value={quantityInStock} onChange={(e) => setQuantityInStock(e.target.value)} required />
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">Salvar</button>
          <button type="button" className="btn btn-secondary" onClick={() => navigate('/raw-materials')}>Cancelar</button>
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
