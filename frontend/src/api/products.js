import client from './client';

export async function fetchProducts() {
  const { data } = await client.get('/products');
  return data;
}

export async function fetchProductById(id) {
  const { data } = await client.get(`/products/${id}`);
  return data;
}

export async function createProduct(product) {
  const { data } = await client.post('/products', product);
  return data;
}

export async function updateProduct(id, product) {
  const { data } = await client.put(`/products/${id}`, product);
  return data;
}

export async function deleteProduct(id) {
  await client.delete(`/products/${id}`);
}
