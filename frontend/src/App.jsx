import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Provider } from 'react-redux';
import { store } from './store';
import Layout from './pages/Layout';
import ProductList from './pages/ProductList';
import ProductForm from './pages/ProductForm';
import RawMaterialList from './pages/RawMaterialList';
import RawMaterialForm from './pages/RawMaterialForm';
import ProductionSuggestion from './pages/ProductionSuggestion';
import './App.css';

function App() {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<ProductList />} />
            <Route path="products/new" element={<ProductForm />} />
            <Route path="products/:id" element={<ProductForm />} />
            <Route path="raw-materials" element={<RawMaterialList />} />
            <Route path="raw-materials/new" element={<RawMaterialForm />} />
            <Route path="raw-materials/:id" element={<RawMaterialForm />} />
            <Route path="production-suggestion" element={<ProductionSuggestion />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </Provider>
  );
}

export default App;
