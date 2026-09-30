export const emptyPerformanceData = {
  rows: [],
  marksDistribution: [],
  gradeDistribution: [],
};

export function calculateAverage(values) {
  const scores = values.map(Number).filter(Number.isFinite);
  if (!scores.length) {
    return null;
  }

  return scores.reduce((sum, score) => sum + score, 0) / scores.length;
}

export function calculateHighestScore(values) {
  const scores = values.map(Number).filter(Number.isFinite);
  return scores.length ? Math.max(...scores) : null;
}

export function calculateLowestScore(values) {
  const scores = values.map(Number).filter(Number.isFinite);
  return scores.length ? Math.min(...scores) : null;
}

export function calculatePassRate(values, passMark) {
  const scores = values.map(Number).filter(Number.isFinite);
  const threshold = Number(passMark);

  if (!scores.length || !Number.isFinite(threshold)) {
    return null;
  }

  const passed = scores.filter((score) => score >= threshold).length;
  return (passed / scores.length) * 100;
}

export function calculateMarksDistribution(results, gradeBands = []) {
  if (!results?.length || !gradeBands?.length) {
    return [];
  }

  return gradeBands.map((band) => ({
    label: band.label,
    color: band.color,
    value: results.filter((result) => {
      const score = Number(result.score ?? result.mark);
      return Number.isFinite(score) && score >= band.min && score <= band.max;
    }).length,
  }));
}

export function calculateGradeDistribution(results, gradeDefinitions = []) {
  if (!results?.length || !gradeDefinitions?.length) {
    return [];
  }

  return gradeDefinitions.map((grade) => ({
    label: grade.label,
    color: grade.color,
    value: results.filter((result) => result.grade === grade.label).length,
  }));
}

export function rankStudents(students, direction = 'desc') {
  return [...(students || [])].sort((first, second) => {
    const firstScore = Number(first.averageScore);
    const secondScore = Number(second.averageScore);

    if (!Number.isFinite(firstScore) && !Number.isFinite(secondScore)) {
      return 0;
    }

    if (!Number.isFinite(firstScore)) {
      return 1;
    }

    if (!Number.isFinite(secondScore)) {
      return -1;
    }

    return direction === 'asc' ? firstScore - secondScore : secondScore - firstScore;
  });
}

export function rankClasses(classes, direction = 'desc') {
  return rankStudents(classes, direction);
}

export function applyPerformanceFilters(rows, filters = {}) {
  const searchTerm = (filters.search || '').trim().toLowerCase();

  return (rows || []).filter((row) => {
    const matchesSearch =
      !searchTerm ||
      [row.studentName, row.admissionNo, row.className]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(searchTerm));

    const matchesClass = !filters.classId || String(row.classId) === String(filters.classId);
    const matchesGrade = !filters.grade || row.grade === filters.grade;

    return matchesSearch && matchesClass && matchesGrade;
  });
}
