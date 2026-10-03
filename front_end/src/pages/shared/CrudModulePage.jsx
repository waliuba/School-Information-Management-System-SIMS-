import { useEffect, useMemo, useState } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
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
    fields:[['studentId','Student','remote:students',true],['departmentId','Department','remote:departments',true],['courseId','Course','remote:courses',true],['semester','Semester','number',true],['enrollmentDate','Enrollment date','date',true]], columns:['studentId','departmentId','courseId','semester','enrollmentDate'] },
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
function formForDepartment(name = '') {
  const forms = {
    education: 1, business: 1, hospitality: 1, 'general studies': 1, 'social sciences': 1,
    computing: 2, 'media studies': 2, 'health sciences': 2, law: 3, agriculture: 3, engineering: 4,
  };
  return forms[name.trim().toLowerCase()];
}
function formForClass(name = '') {
  const match = name.match(/^form\s*(\d+)/i);
  return match ? Number(match[1]) : null;
}

export default function CrudModulePage({ module }) {
  const config = definitions[module]; const { id } = useParams(); const location = useLocation(); const navigate = useNavigate();
  const mode = location.pathname.endsWith('/new') ? 'new' : location.pathname.endsWith('/edit') ? 'edit' : id ? 'detail' : 'list';
  const [rows,setRows]=useState([]); const [record,setRecord]=useState(null); const [options,setOptions]=useState({}); const [loading,setLoading]=useState(true);
  const [error,setError]=useState(null); const [fieldErrors,setFieldErrors]=useState({}); const [notice,setNotice]=useState(''); const [pending,setPending]=useState(false); const [confirmId,setConfirmId]=useState(null); const [values,setValues]=useState({});
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
  const onSubmit=async e=>{e.preventDefault();setPending(true);setFieldErrors({});setError(null);try{const payload={...values};if(module==='teachers'){payload.role='TEACHER';if(mode==='edit')delete payload.password;}if(module==='enrollments'&&mode==='new'){const existing=await getEnrollments();if(existing.some(item=>String(item.studentId)===String(payload.studentId)&&String(item.courseId)===String(payload.courseId)&&String(item.semester)===String(payload.semester))){setError({message:'This student is already enrolled in that course for this semester.'});return;}}await(mode==='new'?config.create(payload):config.update(id,payload));setNotice(`${config.title.slice(0,-1)} saved successfully.`);navigate(listHref);}catch(e){setError(e);setFieldErrors(e.details||{});}finally{setPending(false);}};
  const doDelete=async()=>{setPending(true);try{await config.remove(confirmId);setConfirmId(null);setNotice('Record deleted successfully.');await load();}catch(e){setError(e);}finally{setPending(false);}};
  const renderField=f=>{const [key,label,rawType,required,choices]=f;const type=rawType||'text';const value=fieldValue(f,values[key]);const common={id:key,name:key,required:!!required,value,onChange:e=>setValues(v=>{const next={...v,[key]:type==='checkbox'?e.target.checked:e.target.value};if(key==='departmentId'){if(module==='enrollments')next.courseId='';if(module==='students')next.classId='';}return next;})};let control;
    if(type.startsWith('remote:')){const target=type.slice(7);let optionsFor=options[target]||[];if(target==='courses'&&module==='enrollments'&&values.departmentId)optionsFor=optionsFor.filter(o=>String(o.departmentId)===String(values.departmentId));if(target==='classes'&&module==='students'&&values.departmentId){const department=(options.departments||[]).find(o=>String(o.departmentId)===String(values.departmentId));const form=formForDepartment(department?.departmentName);optionsFor=form?optionsFor.filter(o=>formForClass(o.className)===form):optionsFor.filter(o=>String(o.departmentId)===String(values.departmentId));}control=<select {...common}><option value="">Select {label.toLowerCase()}</option>{optionsFor.map(o=>{const oid=remoteId(target,o);const text=o.className||o.departmentName||`${o.firstName||''} ${o.lastName||''}`.trim()||o.courseName||o.unitName||o.username||oid;return <option key={oid} value={oid}>{text}</option>;})}</select>;}else if(type==='select'){control=<select {...common}><option value="">Select {label.toLowerCase()}</option>{choices.map(v=><option key={v}>{v}</option>)}</select>;}else if(type==='checkbox'){control=<input id={key} name={key} type="checkbox" checked={value} onChange={common.onChange}/>;}else if(type.startsWith('fixed:')){control=<input {...common} value={type.slice(6)} readOnly/>;}else{control=<input {...common} type={type==='text'?'text':type} disabled={mode==='edit'&&module==='teachers'&&key==='password'}/>;}
    return <label className="crud-field" htmlFor={key} key={key}><span>{label}{required?' *':''}</span>{control}{fieldErrors[key]?<small role="alert">{fieldErrors[key]}</small>:null}</label>;};
  if(mode==='list')return <PageContainer title={config.title} description={`Manage ${config.title.toLowerCase()} using the connected SIMS API.`} actions={<Link className="primary-button" to={`${listHref}/new`}>Add {config.title.slice(0,-1)}</Link>}>
    {notice?<p role="status" className="crud-notice">{notice}</p>:null}<ErrorMessage error={error}/>{!error?<Table columns={[...columns,{key:'actions',label:'Actions',render:r=><span className="crud-actions"><Link to={`${listHref}/${r[config.id]}`}>View</Link><Link to={`${listHref}/${r[config.id]}/edit`}>Edit</Link><button type="button" onClick={()=>setConfirmId(r[config.id])}>Delete</button></span>}]} rows={rows} isLoading={loading} emptyMessage={`No ${config.title.toLowerCase()} found.`}/>:null}
    {confirmId!=null?<div className="crud-confirm" role="dialog" aria-modal="true" aria-labelledby="delete-title"><h2 id="delete-title">Delete record?</h2><p>This action cannot be undone. Backend relationship rules may prevent deletion.</p><button disabled={pending} onClick={()=>setConfirmId(null)}>Cancel</button><button disabled={pending} onClick={doDelete}>{pending?'Deleting…':'Confirm delete'}</button></div>:null}
  </PageContainer>;
  if(mode==='detail')return <PageContainer title={`${config.title.slice(0,-1)} details`} actions={<><Link to={`${listHref}/${id}/edit`}>Edit</Link> <Link to={listHref}>Back to list</Link></>}><ErrorMessage error={error}/>{record?<dl className="crud-details">{Object.entries(record).map(([k,v])=><div key={k}><dt>{k.replace(/[A-Z]/g,' $&')}</dt><dd>{labelValue(v)}</dd></div>)}</dl>:null}</PageContainer>;
  return <PageContainer title={mode==='new'?`Add ${config.title.slice(0,-1)}`:`Edit ${config.title.slice(0,-1)}`} actions={<Link to={listHref}>Back to list</Link>}><ErrorMessage error={error}/><form className="crud-form" onSubmit={onSubmit}><div className="crud-fields">{editable.map(renderField)}</div><button className="primary-button" disabled={pending||loading}>{pending?'Saving…':'Save record'}</button></form></PageContainer>;
}
