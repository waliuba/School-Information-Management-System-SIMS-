import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import PageContainer from '../../components/layout/PageContainer.jsx';
import ErrorMessage from '../../components/common/ErrorMessage.jsx';
import Table from '../../components/common/Table.jsx';
import { getResults } from '../../services/api/resourcesApi.js';

export default function ResultsPage(){
  const {id}=useParams();
  const [rows,setRows]=useState([]),[error,setError]=useState(null),[loading,setLoading]=useState(true);
  const load=async()=>{setLoading(true);setError(null);try{setRows(await getResults());}catch(e){setError(e);}finally{setLoading(false);}};
  useEffect(()=>{load();},[]);
  const cols=[['studentName','Student'],['admissionNo','Admission no.'],['courseName','Course'],['score','Score'],['grade','Grade'],['performance','Performance']].map(([key,label])=>({key,label,render:r=>r[key]??'—'}));
  return <PageContainer title={id?'Result details':'Examinations'} description="Review examination results and download them as a PDF." actions={<button className="primary-button no-print" type="button" onClick={()=>window.print()}>Download results as PDF</button>}>
    <ErrorMessage error={error}/>
    <section className="crud-panel results-print-area"><h2>{id?'Result details':'Results'}</h2><Table rows={id?rows.filter(r=>String(r.performanceId)===String(id)):rows} columns={cols} isLoading={loading} emptyMessage="No results found."/></section>
  </PageContainer>;
}  
