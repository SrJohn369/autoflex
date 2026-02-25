import { render, screen, fireEvent } from '@testing-library/react';
import Modal from './Modal';

describe('Modal Component', () => {
    it('renders nothing when isOpen is false', () => {
        const { container } = render(
            <Modal isOpen={false} title="Test Modal" message="Hidden message" type="primary" onConfirm={() => { }} />
        );
        expect(container.firstChild).toBeNull();
    });

    it('renders the title and message when isOpen is true', () => {
        render(
            <Modal isOpen={true} title="Test Title" message="Test Message" type="primary" onConfirm={() => { }} />
        );

        expect(screen.getByText('Test Title')).toBeInTheDocument();
        expect(screen.getByText('Test Message')).toBeInTheDocument();
    });

    it('calls onConfirm when confirm button is clicked', () => {
        const handleConfirm = jest.fn();
        render(
            <Modal isOpen={true} title="Test" message="Test" type="primary" onConfirm={handleConfirm} confirmText="Confirmar" />
        );

        const confirmBtn = screen.getByText('Confirmar');
        fireEvent.click(confirmBtn);
        expect(handleConfirm).toHaveBeenCalledTimes(1);
    });

    it('calls onCancel when cancel button is clicked', () => {
        const handleCancel = jest.fn();
        render(
            <Modal isOpen={true} title="Test" message="Test" type="primary" onConfirm={() => { }} onCancel={handleCancel} />
        );

        const cancelBtn = screen.getByText('Cancelar');
        fireEvent.click(cancelBtn);
        expect(handleCancel).toHaveBeenCalledTimes(1);
    });
});
