import client from './client';

export async function fetchProductionSuggestion() {
  const { data } = await client.get('/production-suggestion');
  return data;
}
