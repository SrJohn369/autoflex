import React from 'react';
import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import productReducer from './store/slices/productSlice';
import rawMaterialReducer from './store/slices/rawMaterialSlice';

export function renderWithProviders(
    ui,
    {
        preloadedState = {},
        store = configureStore({
            reducer: {
                products: productReducer,
                rawMaterials: rawMaterialReducer
            },
            preloadedState
        }),
        ...renderOptions
    } = {}
) {
    function Wrapper({ children }) {
        return (
            <Provider store={store}>
                <BrowserRouter>{children}</BrowserRouter>
            </Provider>
        );
    }
    return { store, ...render(ui, { wrapper: Wrapper, ...renderOptions }) };
}
