import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { loadRawMaterials, removeRawMaterial } from '../store/slices/rawMaterialSlice';
import Modal from '../components/Modal';

export default function RawMaterialList() {
  const dispatch = useDispatch();
  const { list, loading, error } = useSelector((s) => s.rawMaterials);

  const [deleteModal, setDeleteModal] = useState({ isOpen: false, id: null, code: '', name: '' });

  useEffect(() => {
    dispatch(loadRawMaterials());
  }, [dispatch]);

  const requestDelete = (id, code, name) => {
    setDeleteModal({ isOpen: true, id, code, name });
  };

  const confirmDelete = () => {
    if (deleteModal.id) {
      dispatch(removeRawMaterial(deleteModal.id)).then(() => dispatch(loadRawMaterials()));
    }
    setDeleteModal({ isOpen: false, id: null, code: '', name: '' });
  };

  const cancelDelete = () => {
    setDeleteModal({ isOpen: false, id: null, code: '', name: '' });
  };

  if (loading) return <p className="loading">Carregando matérias-primas…</p>;
  if (error) return <p className="error">Erro: {error}</p>;

  return (
    <div className="page">
      <div className="page-header">
        <h2>Matérias-primas</h2>
        <Link to="/raw-materials/new" className="btn btn-primary">Nova matéria-prima</Link>
      </div>
      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th className="actions">Ações</th>
              <th>Código</th>
              <th>Nome</th>
              <th>Quantidade em estoque</th>
            </tr>
          </thead>
          <tbody>
            {list.length === 0 ? (
              <tr><td colSpan={4}>Nenhuma matéria-prima cadastrada.</td></tr>
            ) : (
              list.map((r) => (
                <tr key={r.id}>
                  <td>{r.code}</td>
                  <td>{r.name}</td>
                  <td>{Number(r.quantityInStock).toLocaleString()}</td>
                  <td className="actions">
                    <Link to={`/raw-materials/${r.id}`} className="btn btn-sm">Editar</Link>
                    <button type="button" className="btn btn-sm btn-danger" onClick={() => requestDelete(r.id, r.code, r.name)}>Excluir</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <Modal
        isOpen={deleteModal.isOpen}
        title="Confirmar Exclusão"
        message={`Deseja realmente excluir a matéria-prima "${deleteModal.code} - ${deleteModal.name}"?`}
        type="danger"
        onConfirm={confirmDelete}
        onCancel={cancelDelete}
        confirmText="Excluir"
      />
    </div>
  );
}
