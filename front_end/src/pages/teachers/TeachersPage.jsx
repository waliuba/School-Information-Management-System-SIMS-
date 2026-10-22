import { useEffect, useMemo, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import PageContainer from '../../components/layout/PageContainer.jsx';
import Pagination from '../../components/common/Pagination.jsx';
import { useAuth } from '../../hooks/useAuth.js';
import { getTeachers } from '../../services/api/resourcesApi.js';

const PAGE_SIZES = [10, 25, 50];
const SORT_KEYS = ['username', 'email'];
const statusLabel = (teacher) => teacher.enabled ? 'Active' : 'Inactive';
const safeText = (value) => value == null || value === '' ? '—' : String(value);

export default function TeachersPage() {
  const [params, setParams] = useSearchParams();
  const { user } = useAuth();
  const canAdd = (user?.role || user?.role_name || user?.roleName) === 'ADMIN';
  const search = params.get('search') || '';
  const status = params.get('status') || 'all';
  const sortKey = SORT_KEYS.includes(params.get('sort')) ? params.get('sort') : 'username';
  const sortDirection = params.get('direction') === 'desc' ? 'desc' : 'asc';
  const requestedSize = Number(params.get('size'));
  const pageSize = PAGE_SIZES.includes(requestedSize) ? requestedSize : PAGE_SIZES[0];
  const requestedPage = Number(params.get('page'));
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [error, setError] = useState(false);

  useEffect(() => {
    let active = true;
    setLoading((current) => rows.length === 0);
    setRefreshing(rows.length > 0);
    setError(false);
    getTeachers().then((data) => { if (active) setRows(data); })
      .catch(() => { if (active) setError(true); })
      .finally(() => { if (active) { setLoading(false); setRefreshing(false); } });
    return () => { active = false; };
  // The list is fetched once; filters are applied locally because the API has no query support.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function updateParam(key, value, resetPage = ['search', 'status', 'sort', 'size'].includes(key)) {
    const next = new URLSearchParams(params);
    if (value) next.set(key, value); else next.delete(key);
    if (resetPage) next.delete('page');
    setParams(next, { replace: true });
  }

  function clearFilters() {
    const next = new URLSearchParams(params);
    ['search', 'status', 'page'].forEach((key) => next.delete(key));
    setParams(next, { replace: true });
  }

  function toggleSort(key) {
    const nextDirection = sortKey === key && sortDirection === 'asc' ? 'desc' : 'asc';
    const next = new URLSearchParams(params);
    next.set('sort', key);
    next.set('direction', nextDirection);
    next.delete('page');
    setParams(next, { replace: true });
  }

  const filtered = useMemo(() => rows.filter((teacher) => {
    const query = search.trim().toLowerCase();
    const matchesSearch = !query || [teacher.username, teacher.email].some((value) => String(value || '').toLowerCase().includes(query));
    const matchesStatus = status === 'all' || (status === 'active' ? teacher.enabled : !teacher.enabled);
    return matchesSearch && matchesStatus;
  }).sort((a, b) => {
    const result = String(a[sortKey] || '').localeCompare(String(b[sortKey] || ''), undefined, { numeric: true, sensitivity: 'base' });
    return sortDirection === 'asc' ? result : -result;
  }), [rows, search, status, sortKey, sortDirection]);

  const activeCount = rows.filter((teacher) => teacher.enabled).length;
  const inactiveCount = rows.length - activeCount;
  const hasFilters = !!search || status !== 'all';
  const pageCount = Math.max(1, Math.ceil(filtered.length / pageSize));
  const safeRequestedPage = Number.isInteger(requestedPage) && requestedPage > 0 ? requestedPage : 1;
  const page = loading || refreshing || error ? safeRequestedPage : Math.min(safeRequestedPage, pageCount);
  const pageRows = filtered.slice((page - 1) * pageSize, page * pageSize);

  useEffect(() => {
    if (loading || refreshing || error) return;
    const rawPage = params.get('page');
    const rawSize = params.get('size');
    const rawSort = params.get('sort');
    const rawStatus = params.get('status');
    const sizeIsInvalid = rawSize && !PAGE_SIZES.includes(Number(rawSize));
    const sortIsInvalid = rawSort && !SORT_KEYS.includes(rawSort);
    const statusIsInvalid = rawStatus && !['all', 'active', 'inactive'].includes(rawStatus);
    const pageNeedsUpdate = !((!rawPage && page === 1) || rawPage === String(page));
    if (!sizeIsInvalid && !sortIsInvalid && !statusIsInvalid && !pageNeedsUpdate) return;
    const next = new URLSearchParams(params);
    if (sizeIsInvalid) next.delete('size');
    if (sortIsInvalid) { next.delete('sort'); next.delete('direction'); }
    if (statusIsInvalid) next.delete('status');
    if (page === 1) next.delete('page'); else next.set('page', String(page));
    setParams(next, { replace: true });
  }, [page, params, setParams, loading, refreshing, error]);

  const addLink = canAdd ? <Link className="primary-button" to="/teachers/new">+ Add Teacher</Link> : null;
  return <PageContainer title="Teachers" description="Manage and monitor the school's teaching staff." actions={addLink}>
    <section className="teacher-stats" aria-label="Teacher summary">
      {[['Total teachers', rows.length, 'All teacher accounts', 'TT'], ['Active', activeCount, 'Enabled accounts', 'AC'], ['Inactive', inactiveCount, 'Disabled accounts', 'IN']].map(([label, value, detail, icon]) => <article className="teacher-stat" key={label}><span className="teacher-stat__icon" aria-hidden="true">{icon}</span><div><span>{label}</span><strong>{loading ? '—' : value}</strong><small>{detail}</small></div></article>)}
      {/* TODO(api): Add the Departments card when teacher records expose a department. */}
    </section>

    <section className="teacher-toolbar" aria-label="Teacher filters">
      <label className="teacher-search">Search teachers<input type="search" value={search} onChange={(event) => updateParam('search', event.target.value)} placeholder="Name or email" /></label>
      <label>Status<select value={status} onChange={(event) => updateParam('status', event.target.value)}><option value="all">All statuses</option><option value="active">Active</option><option value="inactive">Inactive</option></select></label>
      {hasFilters && <button className="teacher-clear" type="button" onClick={clearFilters}>Clear filters</button>}
      {/* TODO(api): Department, employee number, phone, and department search are unavailable in the current teacher DTO. */}
    </section>

    <div className="teacher-table-heading"><h2>Teacher records</h2><span aria-live="polite">{loading ? 'Loading teachers…' : `${filtered.length} ${filtered.length === 1 ? 'teacher' : 'teachers'}`}</span>{refreshing && <span className="teacher-refresh" role="status">Updating…</span>}</div>
    {error ? <div className="teacher-error" role="alert"><div><strong>Unable to load teachers</strong><p>We couldn't retrieve the teacher records right now.</p></div><button type="button" onClick={() => {setLoading(true);setError(false);getTeachers().then(setRows).catch(()=>setError(true)).finally(()=>setLoading(false));}}>Retry</button></div> : loading ? <div className="teacher-skeleton" role="status" aria-label="Loading teachers">{Array.from({length:5},(_,i)=><div key={i}><span/><span/><span/></div>)}</div> : filtered.length === 0 ? <div className="teacher-empty"><strong>{rows.length ? 'No teachers match your filters' : 'No teachers found'}</strong><p>{rows.length ? 'Try changing your search or status filter.' : 'Teacher records will appear here when they are added.'}</p>{rows.length ? <button type="button" onClick={clearFilters}>Clear filters</button> : addLink}</div> : <>
      <div className="teacher-table-wrap"><table className="teacher-table"><caption className="sr-only">Teacher records</caption><thead><tr>
        <th scope="col" aria-sort={sortKey === 'username' ? (sortDirection === 'asc' ? 'ascending' : 'descending') : 'none'}><button className="teacher-sort" type="button" aria-label="Sort by teacher" onClick={() => toggleSort('username')}>Teacher<span aria-hidden="true">{sortKey === 'username' ? (sortDirection === 'asc' ? ' ↑' : ' ↓') : ''}</span></button></th>
        <th scope="col" aria-sort={sortKey === 'email' ? (sortDirection === 'asc' ? 'ascending' : 'descending') : 'none'}><button className="teacher-sort" type="button" aria-label="Sort by email" onClick={() => toggleSort('email')}>Email<span aria-hidden="true">{sortKey === 'email' ? (sortDirection === 'asc' ? ' ↑' : ' ↓') : ''}</span></button></th>
        <th scope="col">Department</th><th scope="col">Phone</th><th scope="col">Status</th><th scope="col">Actions</th></tr></thead><tbody>{pageRows.map((teacher) => <tr key={teacher.userId}><td><Link className="teacher-identity" to={`/teachers/${teacher.userId}`}><span className="teacher-avatar" aria-hidden="true">{String(teacher.username || '?').slice(0,1).toUpperCase()}</span><span><strong title={safeText(teacher.username)}>{safeText(teacher.username)}</strong><small>#{safeText(teacher.userId)}</small></span></Link></td><td title={safeText(teacher.email)}>{safeText(teacher.email)}</td><td>—</td><td>—</td><td><span className={`teacher-status ${teacher.enabled ? 'is-active' : 'is-inactive'}`}>{statusLabel(teacher)}</span></td><td><span className="teacher-actions"><Link to={`/teachers/${teacher.userId}`}>View</Link><Link to={`/teachers/${teacher.userId}/edit`}>Edit</Link></span></td></tr>)}</tbody></table></div>
      <div className="teacher-cards">{pageRows.map((teacher) => <article className="teacher-mobile-card" key={teacher.userId}><div className="teacher-mobile-card__heading"><Link to={`/teachers/${teacher.userId}`}><strong>{safeText(teacher.username)}</strong><small>#{safeText(teacher.userId)}</small></Link><span className={`teacher-status ${teacher.enabled ? 'is-active' : 'is-inactive'}`}>{statusLabel(teacher)}</span></div><p>{safeText(teacher.email)}</p><small>Department —</small><div className="teacher-actions"><Link to={`/teachers/${teacher.userId}`}>View</Link><Link to={`/teachers/${teacher.userId}/edit`}>Edit</Link></div></article>)}</div>
      <Pagination total={filtered.length} page={page} pageSize={pageSize} onPageChange={(nextPage) => updateParam('page', nextPage === 1 ? '' : String(nextPage), false)} onPageSizeChange={(nextSize) => updateParam('size', nextSize === PAGE_SIZES[0] ? '' : String(nextSize))} pageSizeOptions={PAGE_SIZES} />
    </>}
  </PageContainer>;
}
