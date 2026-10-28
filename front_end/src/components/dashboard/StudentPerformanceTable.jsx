import { useEffect, useRef, useState } from 'react';
import PerformanceTable from './PerformanceTable.jsx';
import Pagination from '../common/Pagination.jsx';
import { getPreferredPageSize, PAGE_SIZE_OPTIONS } from '../../services/preferences.js';

const EMPTY_ROWS = [];

const columns = [
  { key: 'studentName', label: 'Student' },
  { key: 'admissionNo', label: 'Admission No.' },
  { key: 'className', label: 'Class/Stream' },
  { key: 'averageScore', label: 'Average Score' },
  { key: 'grade', label: 'Grade' },
  { key: 'performance', label: 'Performance' },
];

export default function StudentPerformanceTable({ rows }) {
  const safeRows = Array.isArray(rows) ? rows : EMPTY_ROWS;
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(getPreferredPageSize);
  const previousRows = useRef(safeRows);
  const pageCount = Math.max(1, Math.ceil(safeRows.length / pageSize));
  const effectivePage = Math.min(page, pageCount);
  const visibleRows = safeRows.slice((effectivePage - 1) * pageSize, effectivePage * pageSize);

  useEffect(() => {
    if (previousRows.current !== safeRows) {
      previousRows.current = safeRows;
      setPage(1);
      return;
    }
    if (page > pageCount) setPage(pageCount);
  }, [safeRows, page, pageCount]);

  return (
    <>
      <PerformanceTable
        columns={columns}
        rows={visibleRows}
        emptyTitle="No student performance data available yet"
        emptyMessage="Student averages, grades, and support indicators will appear once examination results are available."
      />
      {safeRows.length > 0 && <Pagination
        total={safeRows.length}
        page={effectivePage}
        pageSize={pageSize}
        onPageChange={setPage}
        onPageSizeChange={(size) => { setPageSize(size); setPage(1); }}
        pageSizeOptions={PAGE_SIZE_OPTIONS}
      />}
    </>
  );
}
