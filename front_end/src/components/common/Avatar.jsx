export default function Avatar({ name, src, className = '' }) {
  const initials = String(name || '?').trim().slice(0, 1).toUpperCase() || '?';

  return (
    <span className={`avatar ${className}`.trim()} aria-hidden="true">
      {src ? <img src={src} alt="" /> : initials}
    </span>
  );
}
