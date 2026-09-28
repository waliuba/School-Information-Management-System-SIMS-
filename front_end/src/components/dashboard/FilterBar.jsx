const defaultFilters = {
  search: '',
  academicYear: '',
  classId: '',
  subjectId: '',
  grade: '',
  performance: 'all',
  sort: 'highest',
  topLimit: 'all',
};

export default function FilterBar({
  filters = defaultFilters,
  onChange,
  onClear,
  classes = [],
  subjects = [],
  grades = [],
  showTopLimit = false,
}) {
  const updateFilter = (key) => (event) => {
    onChange?.({ ...filters, [key]: event.target.value });
  };

  return (
    <section className="filter-bar" aria-label="Dashboard filters">
      <label className="filter-field filter-field--wide">
        <span>Search</span>
        <input
          type="search"
          value={filters.search || ''}
          onChange={updateFilter('search')}
          placeholder="Name or admission no."
        />
      </label>

      <label className="filter-field">
        <span>Academic Year</span>
        <input
          type="text"
          value={filters.academicYear || ''}
          onChange={updateFilter('academicYear')}
          placeholder="Any year"
        />
      </label>

      <label className="filter-field">
        <span>Class/Stream</span>
        <select value={filters.classId || ''} onChange={updateFilter('classId')}>
          <option value="">All classes</option>
          {classes.map((classItem) => (
            <option key={classItem.classId} value={classItem.classId}>
              {classItem.className}
            </option>
          ))}
        </select>
      </label>

      <label className="filter-field">
        <span>Subject</span>
        <select value={filters.subjectId || ''} onChange={updateFilter('subjectId')}>
          <option value="">All subjects</option>
          {subjects.map((subject) => (
            <option key={subject.courseId || subject.unitId} value={subject.courseId || subject.unitId}>
              {subject.courseName || subject.unitName}
            </option>
          ))}
        </select>
      </label>

      <label className="filter-field">
        <span>Grade</span>
        <select value={filters.grade || ''} onChange={updateFilter('grade')}>
          <option value="">All grades</option>
          {grades.map((grade) => (
            <option key={grade} value={grade}>
              {grade}
            </option>
          ))}
        </select>
      </label>

      <label className="filter-field">
        <span>Performance</span>
        <select value={filters.performance || 'all'} onChange={updateFilter('performance')}>
          <option value="all">All Students</option>
          <option value="top">Top Performing</option>
          <option value="support">Needs Attention</option>
        </select>
      </label>

      {showTopLimit ? (
        <label className="filter-field">
          <span>Limit</span>
          <select value={filters.topLimit || 'all'} onChange={updateFilter('topLimit')}>
            <option value="all">All</option>
            <option value="5">Top 5</option>
            <option value="10">Top 10</option>
            <option value="20">Top 20</option>
          </select>
        </label>
      ) : null}

      <label className="filter-field">
        <span>Sort</span>
        <select value={filters.sort || 'highest'} onChange={updateFilter('sort')}>
          <option value="highest">Highest Score</option>
          <option value="lowest">Lowest Score</option>
          <option value="grade">Grade</option>
        </select>
      </label>

      <button className="filter-bar__clear" type="button" onClick={onClear}>
        Clear Filters
      </button>
    </section>
  );
}
