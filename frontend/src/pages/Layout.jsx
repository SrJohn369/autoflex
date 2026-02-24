import { Link, Outlet } from 'react-router-dom';

export default function Layout() {
  return (
    <div className="layout">
      <header className="header">
        <h1 className="brand">Autoflex – Controle de Estoque</h1>
        <nav className="nav">
          <Link to="/">Produtos</Link>
          <Link to="/raw-materials">Matérias-primas</Link>
          <Link to="/production-suggestion">Sugestão de Produção</Link>
        </nav>
      </header>
      <main className="main">
        <Outlet />
      </main>
    </div>
  );
}
