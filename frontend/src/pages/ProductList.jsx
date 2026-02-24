import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { loadProducts, removeProduct } from '../store/slices/productSlice';
import Modal from '../components/Modal';

export default function ProductList() {
  const dispatch = useDispatch();
  const { list, loading, error } = useSelector((s) => s.products);

  const [deleteModal, setDeleteModal] = useState({ isOpen: false, id: null, code: '', name: '' });

  useEffect(() => {
    dispatch(loadProducts());
  }, [dispatch]);

  const requestDelete = (id, code, name) => {
    setDeleteModal({ isOpen: true, id, code, name });
  };

  const confirmDelete = () => {
    if (deleteModal.id) {
      dispatch(removeProduct(deleteModal.id)).then(() => dispatch(loadProducts()));
    }
    setDeleteModal({ isOpen: false, id: null, code: '', name: '' });
  };

  const cancelDelete = () => {
    setDeleteModal({ isOpen: false, id: null, code: '', name: '' });
  };

  if (loading) return <p className="loading">Carregando produtos…</p>;
  if (error) return <p className="error">Erro: {error}</p>;

  return (
    <div className="page">
      <div className="page-header">
        <h2>Produtos</h2>
        <Link to="/products/new" className="btn btn-primary">Novo produto</Link>
      </div>
      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Nome</th>
              <th>Valor</th>
              <th>Matérias-primas</th>
              <th className="actions">Ações</th>
            </tr>
          </thead>
          <tbody>
            {list.length === 0 ? (
              <tr><td colSpan={5}>Nenhum produto cadastrado.</td></tr>
            ) : (
              list.map((p) => (
                <tr key={p.id}>
                  <td>{p.code}</td>
                  <td>{p.name}</td>
                  <td>{Number(p.value).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</td>
                  <td>{p.materials?.length ?? 0} itens</td>
                  <td className="actions">
                    <Link to={`/products/${p.id}`} className="btn btn-sm">Editar</Link>
                    <button type="button" className="btn btn-sm btn-danger" onClick={() => requestDelete(p.id, p.code, p.name)}>Excluir</button>
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
        message={`Deseja realmente excluir o produto "${deleteModal.code} - ${deleteModal.name}"?`}
        type="danger"
        onConfirm={confirmDelete}
        onCancel={cancelDelete}
        confirmText="Excluir"
      />
    </div>
  );
}
