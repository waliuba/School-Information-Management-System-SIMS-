import { useEffect, useId, useRef } from 'react';
import Button from './Button.jsx';

export default function Modal({ title, children, isOpen, onClose, initialFocusRef, showCloseButton = true, descriptionId }) {
  const dialogRef = useRef(null);
  const titleId = useId();
  const closeHandlerRef = useRef(onClose);
  closeHandlerRef.current = onClose;

  useEffect(() => {
    if (!isOpen) return undefined;

    const previousFocus = document.activeElement;
    const dialog = dialogRef.current;
    const getFocusable = () => [...dialog.querySelectorAll(
      'a[href], button:not([disabled]), input:not([disabled]), select:not([disabled]), textarea:not([disabled]), [tabindex]:not([tabindex="-1"])'
    )].filter((element) => element.getClientRects().length > 0);

    const focusFrame = requestAnimationFrame(() => {
      const target = initialFocusRef?.current || getFocusable()[0] || dialog;
      target?.focus();
    });

    function onKeyDown(event) {
      if (event.key === 'Escape') {
        event.preventDefault();
        closeHandlerRef.current?.();
        return;
      }
      if (event.key !== 'Tab') return;

      const focusable = getFocusable();
      if (!focusable.length) {
        event.preventDefault();
        dialog.focus();
        return;
      }

      const first = focusable[0];
      const last = focusable[focusable.length - 1];
      if (!dialog.contains(document.activeElement)) {
        event.preventDefault();
        (event.shiftKey ? last : first).focus();
      } else if (event.shiftKey && document.activeElement === first) {
        event.preventDefault();
        last.focus();
      } else if (!event.shiftKey && document.activeElement === last) {
        event.preventDefault();
        first.focus();
      }
    }

    document.addEventListener('keydown', onKeyDown);
    return () => {
      cancelAnimationFrame(focusFrame);
      document.removeEventListener('keydown', onKeyDown);
      if (previousFocus?.isConnected) previousFocus.focus();
    };
  }, [isOpen, initialFocusRef]);

  if (!isOpen) {
    return null;
  }

  return (
    <div className="modal-backdrop" role="presentation">
      <section ref={dialogRef} className="modal" role="dialog" aria-modal="true" aria-labelledby={titleId} aria-describedby={descriptionId} tabIndex={-1}>
        <header className="modal__header">
          <h2 id={titleId}>{title}</h2>
          {showCloseButton && <Button type="button" variant="secondary" onClick={onClose}>Close</Button>}
        </header>
        {children}
      </section>
    </div>
  );
}
