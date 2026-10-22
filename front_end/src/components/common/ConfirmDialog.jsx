import { useId, useRef } from 'react';
import ErrorMessage from './ErrorMessage.jsx';
import Modal from './Modal.jsx';

export default function ConfirmDialog({
  title,
  children,
  isOpen,
  onCancel,
  onConfirm,
  isBusy = false,
  error,
  cancelLabel = 'Cancel',
  confirmLabel = 'Confirm',
  busyLabel = 'Working…',
}) {
  const cancelRef = useRef(null);
  const descriptionId = useId();
  const closeWhenIdle = isBusy ? () => {} : onCancel;

  return (
    <Modal title={title} isOpen={isOpen} onClose={closeWhenIdle} initialFocusRef={cancelRef} showCloseButton={false} descriptionId={descriptionId}>
      <div className="confirm-dialog__body">
        <p id={descriptionId}>{children}</p>
        <ErrorMessage error={error} />
        <div className="confirm-dialog__actions">
          <button ref={cancelRef} className="button button--secondary" type="button" disabled={isBusy} onClick={onCancel}>
            {cancelLabel}
          </button>
          <button className="button button--danger" type="button" disabled={isBusy} onClick={onConfirm}>
            {isBusy ? busyLabel : confirmLabel}
          </button>
        </div>
      </div>
    </Modal>
  );
}
