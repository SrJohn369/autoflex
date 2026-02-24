import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { loadRawMaterials, removeRawMaterial } from '../store/slices/rawMaterialSlice';

export default function RawMaterialList() {
  const dispatch = useDispatch();
  const { list, loading, error } = useSelector((s) => s.rawMaterials);

  useEffect(() => {
    dispatch(loadRawMaterials());
  }, [dispatch]);

  const handleDelete = (id, code) => {
    if (window.confirm(`Excluir matéria-prima "${code}"?`)) {
      dispatch(removeRawMaterial(id)).then(() => dispatch(loadRawMaterials()));
    }
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
              <th>Código</th>
              <th>Nome</th>
              <th>Quantidade em estoque</th>
              <th className="actions">Ações</th>
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
                    <button type="button" className="btn btn-sm btn-danger" onClick={() => handleDelete(r.id, r.code)}>Excluir</button>
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
