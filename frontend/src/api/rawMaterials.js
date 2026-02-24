import client from './client';

export async function fetchRawMaterials() {
  const { data } = await client.get('/raw-materials');
  return data;
}

export async function fetchRawMaterialById(id) {
  const { data } = await client.get(`/raw-materials/${id}`);
  return data;
}

export async function createRawMaterial(rawMaterial) {
  const { data } = await client.post('/raw-materials', rawMaterial);
  return data;
}

export async function updateRawMaterial(id, rawMaterial) {
  const { data } = await client.put(`/raw-materials/${id}`, rawMaterial);
  return data;
}

export async function deleteRawMaterial(id) {
  await client.delete(`/raw-materials/${id}`);
}
