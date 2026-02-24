import React from 'react';
import './Modal.css';

export default function Modal({ isOpen, title, message, type = 'info', onConfirm, onCancel, confirmText = 'Confirmar', cancelText = 'Cancelar' }) {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className={`modal-content ${type}`}>
        {title && <h3 className="modal-title">{title}</h3>}
        <div className="modal-body">
          <p>{message}</p>
        </div>
        <div className="modal-actions">
          {onCancel && (
            <button type="button" className="btn btn-secondary" onClick={onCancel}>
              {cancelText}
            </button>
          )}
          {onConfirm && (
            <button type="button" className={`btn ${type === 'danger' ? 'btn-danger' : 'btn-primary'}`} onClick={onConfirm}>
              {confirmText}
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
