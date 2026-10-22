const tones = new Set(['neutral', 'success', 'warning', 'danger', 'info']);

export default function Badge({ tone = 'neutral', className = '', children, ...props }) {
  const safeTone = tones.has(tone) ? tone : 'neutral';

  return (
    <span className={`badge badge--${safeTone} ${className}`.trim()} {...props}>
      {children}
    </span>
  );
}
