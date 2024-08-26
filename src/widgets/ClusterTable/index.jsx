import React, { useEffect, useState } from 'react'
import StyledTable from './styles'
import { CLUSTER_COLUMN_DEFS } from '../../constants/columnDefs'
import Empty from '../../components/Empty'
import usePagination from '../../hooks/usePagination'
import Pagination from '../../ui/Pagination'
import axios from 'axios'
import { toast } from 'react-toastify'
import CalendarSelector from '../../components/CalendarSelector'
import Search from '../../ui/Search'
import { useSearchParams } from 'react-router-dom'
import dayjs from 'dayjs'

const TYPE = {
  EVENT: 'event',
  DOCUMENT: 'document',
  CLUSTER: 'cluster'
}

const ClusterTable = () => {
  const [filterData, setFilterData] = useState([]);
  const [isDisabled, setIsDisabled] = useState(true);
  const [selected, setSelected] = useState([]);
  const [date, setDate] = useState();
  const [title, setTitle] = useState('');
  const [searchParams, setSearchParams] = useSearchParams();

  useEffect(() => {    
    searchParams.get("from") && setDate([dayjs(searchParams.get("from"), 'YYYY-MM-DD'), dayjs(searchParams.get("to"), 'YYYY-MM-DD')]);
    searchParams.get("title") && setTitle(searchParams.get("title"));

  }, [])

  useEffect(() => {
    if(!date) {
      setSearchParams({
        title: title,
      })
    }
    else{
      setSearchParams({
        from: date[0].format('YYYY-MM-DDTHH:mm:ss'),
        to: date[1].format('YYYY-MM-DDTHH:mm:ss'),
        title: title,
      })
    }
  }, [date, title])


  const handleMerge = async () => {
    if(selected.length !== 2) return

    console.log(selected)
    const params = "id1=" + selected.map(item => item.id).join('&id2=')
    console.log(params);
    
    const res = await axios.post(`/api/v1/clusters/merge-clusters?${params}`)
    console.log(res.data)
    if(res.data) {
      toast.success("CLUSTER MERGED SUCCESSFULLY")
      fetchCluster({
        page: pagination.currentPage
      })
    }
  }

  const pagination = usePagination(filterData?.num_clusters);
  
  const fetchCluster = async ({ page = 0, date, title = '' }) => {
    let from = '', to = ''
    if(date) {
      from = date[0].format('YYYY-MM-DDTHH:mm:ss')
      to = date[1].format('YYYY-MM-DDTHH:mm:ss')
    }
    const res = await axios.get(`/api/v1/clusters?page=${page}&from=${from}&to=${to}&title=${title}`);
    setFilterData({
      num_clusters: res.data.total,
      clusters: res.data.clusterEntityList.map((cluster, k) => {
        return {
          ...cluster,
          key: `${cluster.id}-${TYPE.CLUSTER}-${k}`,
          children: [
            ...cluster.events.map((event, index) => {
              return {
                ...event,
                key: `${cluster.id}-${TYPE.EVENT}-${index}`,
                text: <>
                        <span className='text-red'>(EVENT) </span> {event.text}
                      </>,
                content: event.text,
              }
            }),
            ...cluster.documents.map((document, index) => {
              return {
                ...document,
                key: `${cluster.id}-${TYPE.DOCUMENT}-${index}`,
                text: <>
                        <span className='text-green'>
                          (DOCUMENT) 
                        </span> {document.text}
                      </>,  
                content: document.text,
              }
            })
          ]
        }
      })
    })
  }

  useEffect(() => {
    
    fetchCluster({
      page: pagination.currentPage,
      date: date,
      title: title,
    })
    
  }, [pagination.currentPage, date, title])

  useEffect(() => {
    pagination.goToPage(0);
  }, [pagination.maxPage])

  const handleDeleteCluster = async (record) => {
    try {
      const res = await axios.delete(`/api/v1/clusters/${record.id}`)

      if(res.data) {
        toast.success("CLUSTER DELETED SUCCESSFULLY")
        const newClusters = filterData.clusters.filter(cluster => cluster.id !== record.id)
        setFilterData({
          ...filterData,
          clusters: newClusters
        })
      }
    } catch (error) {
      toast.error(error.message)
    }
  }

  const handleDeleteEvent = async (record, id) => {
    const res = await axios.delete(`/api/v1/clusters/${id}/events`, {
      data: [record.content]
    })

    if(res.data) {
      toast.success("EVENT DELETED SUCCESSFULLY")
      // Tìm cluster có id khớp với id được truyền vào
      const cluster = filterData.clusters.find(cluster => cluster.id === id);
    
      // Nếu không tìm thấy cluster, hãy trả về
      if (!cluster) return;
    
      // Tạo một mảng mới không chứa record được truyền vào
      const newEvents = cluster.children.filter(child => child.key !== record.key);
    
      // Tạo một cluster mới với mảng children mới
      const newCluster = {
        ...cluster,
        children: newEvents
      };
    
      // Tạo một mảng mới clusters, thay thế cluster cũ bằng cluster mới
      const newClusters = filterData.clusters.map(c => (c.id === id ? newCluster : c));
    
      // Cập nhật filterData với mảng clusters mới
      setFilterData({
        ...filterData,
        clusters: newClusters
      });

    }
  }

  const handleDeleteDocument = async (record, id) => {

    const res = await axios.delete(`/api/v1/clusters/${id}/documents`, {
      data: [record.content]
    })

    if(res.data) {
      toast.success("DOCUMENTS DELETED SUCCESSFULLY")
      // Tìm cluster có id khớp với id được truyền vào
      const cluster = filterData.clusters.find(cluster => cluster.id === id);
    
      // Nếu không tìm thấy cluster, hãy trả về
      if (!cluster) return;
    
      // Tạo một mảng mới không chứa record được truyền vào
      const newDocuments = cluster.children.filter(child => child.key !== record.key);
    
      // Tạo một cluster mới với mảng children mới
      const newCluster = {
        ...cluster,
        children: newDocuments
      };
    
      // Tạo một mảng mới clusters, thay thế cluster cũ bằng cluster mới
      const newClusters = filterData.clusters.map(c => (c.id === id ? newCluster : c));
    
      // Cập nhật filterData với mảng clusters mới
      setFilterData({
        ...filterData,
        clusters: newClusters
      });

    }
  };

  const handleDelete = async (record) => {
    
    const key = record.key
    const id = key.split('-')[0]
    const type = key.split('-')[1]
    const index = key.split('-')[2]

    console.log(id, type, index)

    if(type === TYPE.EVENT) {
      handleDeleteEvent(record, id)
    }

    if(type === TYPE.DOCUMENT) {
      handleDeleteDocument(record, id)
    }

    if(type === TYPE.CLUSTER) {
      handleDeleteCluster(record)
    }


    // fetchCluster({
    //   page: pagination.currentPage
    // })

    

  }

  const handleOnSelect = (record, selected, selectedRows) => {
    console.log(record, selected, selectedRows);
    console.log(selectedRows.length)
    if(selectedRows.length !== 2) {
      setIsDisabled(true);
    }else {
      setIsDisabled(false);
    }
  }

  const handleOnChange = (selectedRowKeys, selectedRows) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
    if(selectedRowKeys.length !== 2) {
      setSelected([])
    }else {
      setSelected(selectedRows)
    }
  }

  const rowSelection = {
    onSelect: handleOnSelect,
    onChange: handleOnChange,
  };

  return (
    <>
      <div className="w-full grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-[26px] lg:grid-cols-9 lg:items-end xl:grid-cols-12">
        <button disabled={isDisabled} className='btn btn--secondary blue !h-[44px] xl:col-span-12' onClick={handleMerge}>Merge Cluster</button>
      </div>
      <div className="w-full grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-[26px] lg:grid-cols-9 lg:items-end xl:grid-cols-12">
        <CalendarSelector value={date} onChange={setDate} wrapperClass="lg:max-w-[275px] lg:col-span-3 xl:col-span-4" id="ordersPeriodSelector"/>
        <Search query={title} setQuery={setTitle} placeholder='Title' wrapperClass="lg:max-w-[275px] lg:col-span-3 xl:col-span-4" />
        <button className='btn btn--outline blue !h-[44px] xl:col-span-1' onClick={() => handleClear()}>
          Clear
        </button>
      </div>
      <div>
        <StyledTable
            columns={CLUSTER_COLUMN_DEFS(handleDelete)}
            pagination={false}
            dataSource={filterData?.clusters}
            locale={{
              emptyText: <Empty text="No items found"/>
            }}
            // rowKey={(record) => (record.title)}
            rowSelection={rowSelection}
        />
      </div>
      {
        pagination.maxPage > 0 && <Pagination pagination={pagination} />
      }
    </>
  )
}

export default ClusterTable