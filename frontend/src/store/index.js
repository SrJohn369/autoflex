import { configureStore } from '@reduxjs/toolkit';
import productReducer from './slices/productSlice';
import rawMaterialReducer from './slices/rawMaterialSlice';

export const store = configureStore({
  reducer: {
    products: productReducer,
    rawMaterials: rawMaterialReducer,
  },
});
