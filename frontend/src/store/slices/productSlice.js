import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import * as productApi from '../../api/products';

export const loadProducts = createAsyncThunk('products/load', () => productApi.fetchProducts());
export const loadProductById = createAsyncThunk('products/loadById', (id) => productApi.fetchProductById(id));
export const saveProduct = createAsyncThunk('products/save', async ({ id, product }) => {
  if (id) return productApi.updateProduct(id, product);
  return productApi.createProduct(product);
});
export const removeProduct = createAsyncThunk('products/remove', (id) => productApi.deleteProduct(id));

const productSlice = createSlice({
  name: 'products',
  initialState: { list: [], current: null, loading: false, error: null },
  reducers: {
    clearCurrent: (state) => {
      state.current = null;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loadProducts.pending, (state) => { state.loading = true; state.error = null; })
      .addCase(loadProducts.fulfilled, (state, action) => { state.list = action.payload; state.loading = false; })
      .addCase(loadProducts.rejected, (state, action) => { state.error = action.error.message; state.loading = false; })
      .addCase(loadProductById.fulfilled, (state, action) => { state.current = action.payload; })
      .addCase(loadProductById.rejected, (state, action) => { state.error = action.error.message; })
      .addCase(saveProduct.fulfilled, (state, action) => { state.current = action.payload; })
      .addCase(saveProduct.rejected, (state, action) => { state.error = action.error.message; })
      .addCase(removeProduct.fulfilled, (state) => { state.current = null; });
  },
});

export const { clearCurrent } = productSlice.actions;
export default productSlice.reducer;
