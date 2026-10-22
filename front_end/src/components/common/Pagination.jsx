export default function Pagination({
  total,
  page,
  pageSize,
  onPageChange,
  onPageSizeChange,
  pageSizeOptions = [10, 25, 50],
}) {
  const pageCount = Math.max(1, Math.ceil(total / pageSize));
  const start = total === 0 ? 0 : (page - 1) * pageSize + 1;
  const end = Math.min(page * pageSize, total);

  return (
    <nav className="pagination" aria-label="Pagination">
      <p className="pagination__count" aria-live="polite">Showing {start}–{end} of {total}</p>
      <label className="pagination__size">
        <span>Rows per page</span>
        <select value={pageSize} onChange={(event) => onPageSizeChange(Number(event.target.value))}>
          {pageSizeOptions.map((size) => <option key={size} value={size}>{size}</option>)}
        </select>
      </label>
      <div className="pagination__controls">
        <button type="button" className="button button--secondary" aria-label="Previous page" disabled={page <= 1} onClick={() => onPageChange(page - 1)}>Previous</button>
        <span aria-current="page">Page {page} of {pageCount}</span>
        <button type="button" className="button button--secondary" aria-label="Next page" disabled={page >= pageCount} onClick={() => onPageChange(page + 1)}>Next</button>
      </div>
    </nav>
  );
}
