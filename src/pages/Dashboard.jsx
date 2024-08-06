import React, { useEffect, useState } from 'react'
import PageHeader from '../layouts/PageHeader'
import DashboardTable from '../widgets/DashboardTable'
import CalendarSelector from '../components/CalendarSelector'
import Search from '../ui/Search'
import { useSearchParams } from 'react-router-dom'
import dayjs from 'dayjs'
import useAPI from '../hooks/useAPI'
import axios from 'axios'
import StyledSelect from '../components/StyledSelect'
import { convertArray, convertString, returnOtions } from '../utils'
const Dashboard = () => {
  
  const [date, setDate] = useState();
  const [title, setTitle] = useState('');
  const [tag, setTag] = useState([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const { data:tags } = useAPI('/api/v1/posts/tags');
    
  const [isDisabled, setIsDisabled] = useState(true);

  const handleFetchStatusCrawl = async () => {
    const res = await axios.get('/api/v1/crawl/state')
    setIsDisabled(res.data)
  }

  useEffect(() => {    
    searchParams.get("from") && setDate([dayjs(searchParams.get("from"), 'YYYY-MM-DD'), dayjs(searchParams.get("to"), 'YYYY-MM-DD')]);
    searchParams.get("title") && setTitle(searchParams.get("title"));
    searchParams.get("tag") && setTag(convertArray(searchParams.get("tag"), ','));
    
    handleFetchStatusCrawl();
  }, [])

  useEffect(() => {
    if(!date) {
      setSearchParams({
        title: title,
        tag: convertString(tag, ',')
      })
    }
    else{
      setSearchParams({
        from: date[0].format('YYYY-MM-DDTHH:mm:ss'),
        to: date[1].format('YYYY-MM-DDTHH:mm:ss'),
        title: title,
        tag: convertString(tag, ',')
      })
    }
  }, [date, title, tag])


  const handleClear = () => {
    setTag('')
    setTitle('')
    setDate()
  }

  const handleCrawl = async () => {
    setIsDisabled(!isDisabled)
    await axios.get('/api/v1/all-apis')
    
  }

  
  return (
    <>
        <PageHeader title="Dashboard"/>
        <div className="flex flex-col flex-1 gap-5 md:gap-[26px]">
          <div className="w-full grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-[26px] lg:grid-cols-9 lg:items-end xl:grid-cols-12">
            <button className='btn btn--outline blue !h-[44px] xl:col-span-12' onClick={handleCrawl}>{!isDisabled ? 'START CRAWL' : 'STOP CRAWL'}</button>
          </div>
          <div className="w-full grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-[26px] lg:grid-cols-9 lg:items-end xl:grid-cols-12">
            <CalendarSelector value={date} onChange={setDate} wrapperClass="lg:max-w-[275px] lg:col-span-3 xl:col-span-4" id="ordersPeriodSelector"/>
            <Search query={title} setQuery={setTitle} placeholder='Title' wrapperClass="lg:max-w-[275px] lg:col-span-3 xl:col-span-4" />
            <StyledSelect query={tag} setQuery={setTag} options={returnOtions(tags)} wrapperClass='lg:max-w-[275px] lg:col-span-3 xl:col-span-3' />
            <button className='btn btn--outline blue !h-[44px] xl:col-span-1' onClick={() => handleClear()}>
              Clear
            </button>
          </div>
          <DashboardTable date={date} title={title} tag={tag} />
        </div>

    </>
  )
}

export default Dashboard