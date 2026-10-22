import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import PageContainer from '../../components/layout/PageContainer.jsx';
import ErrorMessage from '../../components/common/ErrorMessage.jsx';
import Table from '../../components/common/Table.jsx';
import Pagination from '../../components/common/Pagination.jsx';
import { getResults } from '../../services/api/resourcesApi.js';

export default function ResultsPage(){
  const {id}=useParams();
  const [rows,setRows]=useState([]),[error,setError]=useState(null),[loading,setLoading]=useState(true);
  const [page,setPage]=useState(1),[pageSize,setPageSize]=useState(10);
  const load=async()=>{setLoading(true);setError(null);try{setRows(await getResults());}catch(e){setError(e);}finally{setLoading(false);}};
  useEffect(()=>{load();},[]);
  const cols=[['studentName','Student'],['admissionNo','Admission no.'],['courseName','Course'],['score','Score'],['grade','Grade'],['performance','Performance']].map(([key,label])=>({key,label,render:r=>r[key]??'—'}));
  const listRows=id?rows.filter(r=>String(r.performanceId)===String(id)):rows;
  const pageCount=Math.max(1,Math.ceil(listRows.length/pageSize));
  const currentPage=Math.min(page,pageCount);
  const visibleRows=id?listRows:listRows.slice((currentPage-1)*pageSize,currentPage*pageSize);
  useEffect(()=>{setPage(1);},[id]);
  useEffect(()=>{if(page>pageCount)setPage(pageCount);},[page,pageCount]);
  return <PageContainer title={id?'Result details':'Examinations'} description="Review examination results and download them as a PDF." actions={<button className="primary-button no-print" type="button" onClick={()=>window.print()}>Download results as PDF</button>}>
    <ErrorMessage error={error}/>
    <section className="crud-panel results-print-area"><h2>{id?'Result details':'Results'}</h2>
      <div className="no-print"><Table rows={visibleRows} columns={cols} isLoading={loading} emptyMessage="No results found."/>
        {!id&&!error&&!loading&&listRows.length>0&&<Pagination total={listRows.length} page={currentPage} pageSize={pageSize} onPageChange={setPage} onPageSizeChange={size=>{setPageSize(size);setPage(1);}} pageSizeOptions={[10,25,50]}/>}
      </div>
      <div className="results-print-copy"><Table rows={listRows} columns={cols} isLoading={loading} emptyMessage="No results found."/></div>
    </section>
  </PageContainer>;
}  
