import { screen } from '@testing-library/react';
import { renderWithProviders } from '../test-utils';
import RawMaterialList from './RawMaterialList';
import * as actions from '../store/slices/rawMaterialSlice';

jest.mock('../store/slices/rawMaterialSlice', () => {
    const actual = jest.requireActual('../store/slices/rawMaterialSlice');
    return {
        __esModule: true,
        ...actual,
        default: actual.default,
        loadRawMaterials: jest.fn(() => ({ type: 'mock/loadRawMaterials' }))
    };
});

describe('RawMaterialList Component', () => {
    it('renders loading state initially', () => {
        renderWithProviders(<RawMaterialList />, {
            preloadedState: {
                rawMaterials: { list: [], loading: true, error: null }
            }
        });

        expect(screen.getByText('Carregando matérias-primas…')).toBeInTheDocument();
    });

    it('renders error state correctly', () => {
        renderWithProviders(<RawMaterialList />, {
            preloadedState: {
                rawMaterials: { list: [], loading: false, error: 'Network Error' }
            }
        });

        expect(screen.getByText('Erro: Network Error')).toBeInTheDocument();
    });

    it('renders the empty list fallback when there are no items', () => {
        renderWithProviders(<RawMaterialList />, {
            preloadedState: {
                rawMaterials: { list: [], loading: false, error: null }
            }
        });

        expect(screen.getByText('Nenhuma matéria-prima cadastrada.')).toBeInTheDocument();
    });

    it('renders the list of raw materials successfully', () => {
        const mockMaterials = [
            { id: 1, code: 'RM001', name: 'Plastic', quantityInStock: 50 },
            { id: 2, code: 'RM002', name: 'Metal', quantityInStock: 200 }
        ];

        renderWithProviders(<RawMaterialList />, {
            preloadedState: {
                rawMaterials: { list: mockMaterials, loading: false, error: null }
            }
        });

        expect(screen.getByText('RM001')).toBeInTheDocument();
        expect(screen.getByText('Plastic')).toBeInTheDocument();
        expect(screen.getByText('50')).toBeInTheDocument();

        expect(screen.getByText('RM002')).toBeInTheDocument();
        expect(screen.getByText('Metal')).toBeInTheDocument();
        expect(screen.getByText('200')).toBeInTheDocument();
    });
});
