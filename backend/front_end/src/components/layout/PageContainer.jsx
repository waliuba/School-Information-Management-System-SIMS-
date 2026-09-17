export default function PageContainer({ title, description, actions, children }) {
  return (
    <section className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">{title}</h1>
          {description ? <p className="page-description">{description}</p> : null}
        </div>
        {actions ? <div className="page-actions">{actions}</div> : null}
      </div>
      {children}
    </section>
  );
}
