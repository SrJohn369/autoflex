import { useEffect, useState } from 'react';
import { fetchProductionSuggestion } from '../api/productionSuggestion';

export default function ProductionSuggestion() {
  const [data, setData] = useState({ items: [], totalValue: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    fetchProductionSuggestion()
      .then(setData)
      .catch((err) => setError(err.response?.data?.error || err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="loading">Calculando sugestão de produção…</p>;
  if (error) return <p className="error">Erro: {error}</p>;

  const items = data.items || [];
  const totalValue = data.totalValue ?? 0;

  return (
    <div className="page">
      <div className="page-header">
        <h2>Sugestão de Produção</h2>
        <p className="muted">Produtos e quantidades que podem ser produzidos com o estoque atual (priorizando o maior valor).</p>
      </div>
      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Produto</th>
              <th>Valor unitário</th>
              <th>Quantidade produzível</th>
              <th>Valor total</th>
            </tr>
          </thead>
          <tbody>
            {items.length === 0 ? (
              <tr><td colSpan={5}>Nenhum produto pode ser produzido com o estoque atual, ou nenhum produto foi cadastrado ainda.</td></tr>
            ) : (
              items.map((item) => (
                <tr key={item.productId}>
                  <td>{item.productCode}</td>
                  <td>{item.productName}</td>
                  <td>{Number(item.productValue).toLocaleString('en-US', { style: 'currency', currency: 'USD' })}</td>
                  <td>{Number(item.quantityProducible).toLocaleString()}</td>
                  <td>{Number(item.totalValue).toLocaleString('en-US', { style: 'currency', currency: 'USD' })}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
      {items.length > 0 && (
        <p className="total-value">
          <strong>Valor total da sugestão de produção:</strong>{' '}
          {Number(totalValue).toLocaleString('en-US', { style: 'currency', currency: 'USD' })}
        </p>
      )}
    </div>
  );
}
