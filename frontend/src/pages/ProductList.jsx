import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { loadProducts, removeProduct } from '../store/slices/productSlice';

export default function ProductList() {
  const dispatch = useDispatch();
  const { list, loading, error } = useSelector((s) => s.products);

  useEffect(() => {
    dispatch(loadProducts());
  }, [dispatch]);

  const handleDelete = (id, code) => {
    if (window.confirm(`Excluir produto "${code}"?`)) {
      dispatch(removeProduct(id)).then(() => dispatch(loadProducts()));
    }
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
                  <td>{Number(p.value).toLocaleString('en-US', { style: 'currency', currency: 'USD' })}</td>
                  <td>{p.materials?.length ?? 0} itens</td>
                  <td className="actions">
                    <Link to={`/products/${p.id}`} className="btn btn-sm">Editar</Link>
                    <button type="button" className="btn btn-sm btn-danger" onClick={() => handleDelete(p.id, p.code)}>Excluir</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
