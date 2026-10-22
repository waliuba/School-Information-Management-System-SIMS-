import { Children, cloneElement, useId } from 'react';

export default function Tooltip({ content, children }) {
  const tooltipId = useId();
  const child = Children.only(children);
  const describedBy = [child.props['aria-describedby'], tooltipId].filter(Boolean).join(' ');

  return (
    <span className="tooltip">
      {cloneElement(child, { 'aria-describedby': describedBy })}
      <span className="tooltip__content" role="tooltip" id={tooltipId}>{content}</span>
    </span>
  );
}
