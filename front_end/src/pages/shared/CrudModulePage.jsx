import { useEffect, useMemo, useState } from 'react';
import { Link, Navigate, useLocation, useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth.js';
import PageContainer from '../../components/layout/PageContainer.jsx';
import ErrorMessage from '../../components/common/ErrorMessage.jsx';
import Table from '../../components/common/Table.jsx';
import {
  createStudent, updateStudent, deleteStudent, getStudent, getStudents,
  createTeacher, updateTeacher, deleteTeacher, getTeacher, getTeachers,
  createCourse, updateCourse, deleteCourse, getCourse, getCourses,
  createEnrollment, updateEnrollment, deleteEnrollment, getEnrollment, getEnrollments,
  getClasses, getDepartments,
} from '../../services/api/resourcesApi.js';

const remoteLoaders = { classes: getClasses, departments: getDepartments, students: getStudents, courses: getCourses };

const definitions = {
  students: { title: 'Students', id: 'studentId', list: getStudents, get: getStudent, create: createStudent, update: updateStudent, remove: deleteStudent,
    fields: [
      ['admissionNo','Admission number','text',true],['firstName','First name','text',true],['middleName','Middle name'],['lastName','Last name','text',true],['gender','Gender','select',true,['Female','Male','Other']],['dateOfBirth','Date of birth','date',true],['nationalId','National ID','text',true],['email','Email','email',true],['phone','Phone','text',true],['address','Address','text',true],['county','County','text',true],['admissionDate','Admission date','date',true],['classId','Class','remote:classes',true],['departmentId','Department','remote:departments',true],['guardianName','Guardian name','text',true],['guardianPhone','Guardian phone','text',true],['status','Status','text'] ], columns:['admissionNo','firstName','lastName','classId','status'] },
  teachers: { title: 'Teachers', id: 'userId', list: getTeachers, get: getTeacher, create: createTeacher, update: updateTeacher, remove: deleteTeacher,
    fields:[['username','Username (teacher identifier)','text',true],['email','Email','email',true],['password','Password','password',true],['role','Role','fixed:TEACHER'],['enabled','Enabled','checkbox']], columns:['username','email','enabled'] },
  courses: { title: 'Courses', id: 'courseId', list: getCourses, get: getCourse, create: createCourse, update: updateCourse, remove: deleteCourse,
    fields:[['courseName','Course name','text',true],['courseCode','Course code','text',true],['departmentId','Department','remote:departments',true],['description','Description'],['durationYears','Duration (years)','number'],['status','Status','select',false,['Active','Inactive']]], columns:['courseCode','courseName','departmentId','status'] },
  enrollments: { title:'Enrollments', id:'enrollmentId', list:getEnrollments, get:getEnrollment, create:createEnrollment, update:updateEnrollment, remove:deleteEnrollment,
    fields:[['departmentId','Department','remote:departments',true],['studentId','Student','remote:students',true],['courseId','Course','remote:courses',true],['semester','Semester','number',true],['enrollmentDate','Enrollment date','date',true]], columns:['studentId','departmentId','courseId','semester','enrollmentDate'] },
};

function labelValue(value) { return value == null || value === '' ? '—' : String(value); }
function fieldValue(field, value) {
  if (field[2] === 'checkbox') return Boolean(value);
  if (field[2] === 'fixed:TEACHER') return 'TEACHER';
  return value ?? '';
}
function remoteId(target, item) {
  const idFields = { classes: 'classId', departments: 'departmentId', students: 'studentId', courses: 'courseId' };
  return item[idFields[target]];
}
export default function CrudModulePage({ module }) {
  const config = definitions[module]; const { id } = useParams(); const location = useLocation(); const navigate = useNavigate();
  const { user } = useAuth();
  const isAdmin = (user?.role || user?.role_name || user?.roleName) === 'ADMIN';
  const canManage = !['students', 'enrollments'].includes(module) || isAdmin;
  const mode = location.pathname.endsWith('/new') ? 'new' : location.pathname.endsWith('/edit') ? 'edit' : id ? 'detail' : 'list';
  const [rows,setRows]=useState([]); const [record,setRecord]=useState(null); const [options,setOptions]=useState({}); const [loading,setLoading]=useState(true);
  const [error,setError]=useState(null); const [fieldErrors,setFieldErrors]=useState({}); const [notice,setNotice]=useState(''); const [pending,setPending]=useState(false); const [confirmId,setConfirmId]=useState(null); const [values,setValues]=useState({});
  const [studentName,setStudentName]=useState({firstName:'',middleName:''});
  const listHref=`/${module}`;
  const columns=useMemo(()=>config.columns.map(key=>({key,label:key.replace(/[A-Z]/g,' $&').replace(/^./,s=>s.toUpperCase()),render:r=>{
    const remote=config.fields.find(f=>f[0]===key&&f[2]?.startsWith('remote:'));
    if(remote){const target=remote[2].slice(7);const option=(options[target]||[]).find(o=>String(remoteId(target,o))===String(r[key]));if(option)return option.className||option.departmentName||`${option.firstName||''} ${option.lastName||''}`.trim()||option.courseName||option.unitName||labelValue(r[key]);}
    return labelValue(r[key]);
  }})),[config,options]);
  const load=async()=>{setLoading(true);setError(null);try{if(mode==='list') setRows(await config.list()); else if(mode==='detail'||mode==='edit') { const item=await config.get(id);setRecord(item);setValues(item||{}); }}catch(e){setError(e);}finally{setLoading(false);}};
  useEffect(()=>{load();},[module,id,mode]);
  useEffect(()=>{let active=true;const remotes=[...new Set(config.fields.filter(f=>f[2]?.startsWith('remote:')).map(f=>f[2].slice(7)))];Promise.all(remotes.map(async key=>[key,await remoteLoaders[key]()])).then(data=>{if(active)setOptions(Object.fromEntries(data));}).catch(e=>{if(active)setError(e);});return()=>{active=false;};},[module]);
  const editable=config.fields.filter(f=>mode==='new'||f[0]!=='password').filter(f=>!(mode==='edit'&&f[2]==='fixed:TEACHER'));
  const onSubmit=async e=>{e.preventDefault();setPending(true);setFieldErrors({});setError(null);try{const payload={...values};if(module==='teachers'){payload.role='TEACHER';if(mode==='edit')delete payload.password;}if(module==='enrollments'){const student=(options.students||[]).find(item=>String(item.studentId)===String(payload.studentId));const course=(options.courses||[]).find(item=>String(item.courseId)===String(payload.courseId));if(student&&String(student.departmentId)!==String(payload.departmentId)){setError({message:'The selected student belongs to a different department. Update the student record or choose a student from the selected department.'});return;}if(course&&String(course.departmentId)!==String(payload.departmentId)){setError({message:'The selected course belongs to a different department. Choose a course listed under the selected department.'});return;}if(mode==='new'){const existing=await getEnrollments();if(existing.some(item=>String(item.studentId)===String(payload.studentId)&&String(item.courseId)===String(payload.courseId)&&String(item.semester)===String(payload.semester))){setError({message:'This student is already enrolled in that course for this semester.'});return;}}}await(mode==='new'?config.create(payload):config.update(id,payload));setNotice(`${config.title.slice(0,-1)} saved successfully.`);navigate(listHref);}catch(e){setError(e);setFieldErrors(e.details||{});}finally{setPending(false);}};
  const doDelete=async()=>{setPending(true);try{await config.remove(confirmId);setConfirmId(null);setNotice('Record deleted successfully.');await load();}catch(e){setError(e);}finally{setPending(false);}};
  const renderField=f=>{const [key,label,rawType,required,choices]=f;const type=rawType||'text';const value=fieldValue(f,values[key]);const common={id:key,name:key,required:!!required,value,onChange:e=>{const selected=e.target.value;if(key==='departmentId'&&module==='enrollments')setStudentName({firstName:'',middleName:''});setValues(v=>{const next={...v,[key]:type==='checkbox'?e.target.checked:selected};if(key==='departmentId'){if(module==='enrollments'){next.studentId='';next.courseId='';}if(module==='students')next.classId='';}if(key==='studentId'&&module==='enrollments')next.courseId='';if(key==='classId'&&module==='students'){const selectedClass=(options.classes||[]).find(item=>String(item.classId)===String(selected));if(selectedClass?.departmentId!=null)next.departmentId=selectedClass.departmentId;}return next;});}};let control;
    if(module==='enrollments'&&key==='studentId'){
      const allStudents=options.students||[];
      const selectedExistingStudent=allStudents.find(student=>String(student.studentId)===String(values.studentId));
      const students=allStudents.filter(student=>String(student.departmentId)===String(values.departmentId)||(mode==='edit'&&String(student.studentId)===String(values.studentId)));
      const selectedStudent=selectedExistingStudent;
      const firstName=selectedStudent?.firstName??studentName.firstName;
      const middleName=selectedStudent?.middleName??studentName.middleName;
      const firstNames=[...new Set(students.map(student=>student.firstName).filter(Boolean))].sort((a,b)=>a.localeCompare(b));
      const middleNames=[...new Set(students.filter(student=>student.firstName===firstName).map(student=>student.middleName||''))].sort((a,b)=>a.localeCompare(b));
      const matchingStudents=students.filter(student=>student.firstName===firstName&&(student.middleName||'')===middleName);
      const setStudent=(student)=>{setValues(v=>({...v,studentId:student?.studentId??'',courseId:''}));};
      control=<>
        <select id="studentFirstName" name="studentFirstName" aria-label="Student first name" required value={firstName} onChange={e=>{setStudentName({firstName:e.target.value,middleName:''});setStudent(null);}}><option value="">Select first name</option>{firstNames.map(name=><option key={name} value={name}>{name}</option>)}</select>
        <select id="studentMiddleName" name="studentMiddleName" aria-label="Student middle name" value={middleName} onChange={e=>{setStudentName(current=>({...current,middleName:e.target.value}));setStudent(null);}} disabled={!firstName}><option value="">Select middle name</option>{middleNames.map(name=><option key={name||'none'} value={name}>{name||'No middle name'}</option>)}</select>
        <select id="studentLastName" name="studentLastName" aria-label="Student last name" required value={values.studentId||''} onChange={e=>setStudent(matchingStudents.find(student=>String(student.studentId)===e.target.value))} disabled={!firstName}><option value="">Select last name</option>{matchingStudents.map(student=><option key={student.studentId} value={student.studentId}>{student.lastName}{matchingStudents.filter(item=>item.lastName===student.lastName).length>1?` (${student.admissionNo})`:''}</option>)}</select>
      </>;
    }
    if(type.startsWith('remote:')&&!(module==='enrollments'&&key==='studentId')){const target=type.slice(7);let optionsFor=options[target]||[];if(target==='courses'&&module==='enrollments')optionsFor=values.departmentId?optionsFor.filter(o=>String(o.departmentId)===String(values.departmentId)):[];control=<select {...common}><option value="">Select {label.toLowerCase()}</option>{optionsFor.map(o=>{const oid=remoteId(target,o);const department=(options.departments||[]).find(item=>String(item.departmentId)===String(o.departmentId));const text=o.className||o.departmentName||`${o.firstName||''} ${o.lastName||''}`.trim()||o.courseName||o.unitName||o.username||oid;return <option key={oid} value={oid}>{target==='courses'&&department?`${text} — ${department.departmentName}`:text}</option>;})}</select>;}else if(type==='select'){control=<select {...common}><option value="">Select {label.toLowerCase()}</option>{choices.map(v=><option key={v}>{v}</option>)}</select>;}else if(type==='checkbox'){control=<input id={key} name={key} type="checkbox" checked={value} onChange={common.onChange}/>;}else if(type.startsWith('fixed:')){control=<input {...common} value={type.slice(6)} readOnly/>;}else if(!(module==='enrollments'&&key==='studentId')){control=<input {...common} type={type==='text'?'text':type} disabled={mode==='edit'&&module==='teachers'&&key==='password'}/>;}
    if(module==='enrollments'&&key==='studentId')return <div className="crud-field" key={key}><span>{label} *</span>{control}{fieldErrors[key]?<small role="alert">{fieldErrors[key]}</small>:null}</div>;
    return <label className="crud-field" htmlFor={key} key={key}><span>{label}{required?' *':''}</span>{control}{fieldErrors[key]?<small role="alert">{fieldErrors[key]}</small>:null}</label>;};
  if (!canManage && (mode === 'new' || mode === 'edit')) return <Navigate to="/unauthorized" replace />;
  if(mode==='list')return <PageContainer title={config.title} description={`Manage ${config.title.toLowerCase()} using the connected SIMS API.`} actions={canManage?<Link className="primary-button" to={`${listHref}/new`}>Add {config.title.slice(0,-1)}</Link>:null}>
    {notice?<p role="status" className="crud-notice">{notice}</p>:null}<ErrorMessage error={error}/>{!error?<Table columns={[...columns,{key:'actions',label:'Actions',render:r=><span className="crud-actions"><Link to={`${listHref}/${r[config.id]}`}>View</Link>{canManage?<><Link to={`${listHref}/${r[config.id]}/edit`}>Edit</Link><button type="button" onClick={()=>setConfirmId(r[config.id])}>Delete</button></>:null}</span>}]} rows={rows} isLoading={loading} emptyMessage={`No ${config.title.toLowerCase()} found.`}/>:null}
    {confirmId!=null?<div className="crud-confirm" role="dialog" aria-modal="true" aria-labelledby="delete-title"><h2 id="delete-title">Delete record?</h2><p>This action cannot be undone. Backend relationship rules may prevent deletion.</p><button disabled={pending} onClick={()=>setConfirmId(null)}>Cancel</button><button disabled={pending} onClick={doDelete}>{pending?'Deleting…':'Confirm delete'}</button></div>:null}
  </PageContainer>;
  if(mode==='detail')return <PageContainer title={`${config.title.slice(0,-1)} details`} actions={<>{canManage?<Link to={`${listHref}/${id}/edit`}>Edit</Link>:null} <Link to={listHref}>Back to list</Link></>}><ErrorMessage error={error}/>{record?<dl className="crud-details">{Object.entries(record).map(([k,v])=><div key={k}><dt>{k.replace(/[A-Z]/g,' $&')}</dt><dd>{labelValue(v)}</dd></div>)}</dl>:null}</PageContainer>;
  return <PageContainer title={mode==='new'?`Add ${config.title.slice(0,-1)}`:`Edit ${config.title.slice(0,-1)}`} actions={<Link to={listHref}>Back to list</Link>}><ErrorMessage error={error}/><form className="crud-form" onSubmit={onSubmit}><div className="crud-fields">{editable.map(renderField)}</div><button className="primary-button" disabled={pending||loading}>{pending?'Saving…':'Save record'}</button></form></PageContainer>;
}
