import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import * as rawMaterialApi from '../../api/rawMaterials';

export const loadRawMaterials = createAsyncThunk('rawMaterials/load', () => rawMaterialApi.fetchRawMaterials());
export const loadRawMaterialById = createAsyncThunk('rawMaterials/loadById', (id) => rawMaterialApi.fetchRawMaterialById(id));
export const saveRawMaterial = createAsyncThunk('rawMaterials/save', async ({ id, rawMaterial }) => {
  if (id) return rawMaterialApi.updateRawMaterial(id, rawMaterial);
  return rawMaterialApi.createRawMaterial(rawMaterial);
});
export const removeRawMaterial = createAsyncThunk('rawMaterials/remove', (id) => rawMaterialApi.deleteRawMaterial(id));

const rawMaterialSlice = createSlice({
  name: 'rawMaterials',
  initialState: { list: [], current: null, loading: false, error: null },
  reducers: {
    clearCurrent: (state) => {
      state.current = null;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loadRawMaterials.pending, (state) => { state.loading = true; state.error = null; })
      .addCase(loadRawMaterials.fulfilled, (state, action) => { state.list = action.payload; state.loading = false; })
      .addCase(loadRawMaterials.rejected, (state, action) => { state.error = action.error.message; state.loading = false; })
      .addCase(loadRawMaterialById.fulfilled, (state, action) => { state.current = action.payload; })
      .addCase(loadRawMaterialById.rejected, (state, action) => { state.error = action.error.message; })
      .addCase(saveRawMaterial.fulfilled, (state, action) => { state.current = action.payload; })
      .addCase(saveRawMaterial.rejected, (state, action) => { state.error = action.error.message; })
      .addCase(removeRawMaterial.fulfilled, (state) => { state.current = null; });
  },
});

export const { clearCurrent } = rawMaterialSlice.actions;
export default rawMaterialSlice.reducer;
