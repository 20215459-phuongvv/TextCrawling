import React, { useEffect, useState } from 'react'
import { Drawer } from 'antd'
import StyledDrawer from './styles'
import dayjs from 'dayjs'
import StyledTable from '../../widgets/ClusterTable/styles'
import { DOCUMENT_COLUMN_DEFS } from '../../constants/columnDefs'
import Empty from '../../components/Empty'
import axios from 'axios'
import { toast } from 'react-toastify'
const DrawerStyled = ({data, onClose, open, cluster = true }) => {
  const { title, author, time, summary, content, url, text, lastUpdated, 
    documents } = data
    // console.log(children);
  // const filter = children?.filter(child => child.key.includes("-document-"))
  const [filter, setFilter] = useState()
  useEffect(() => {

    setFilter(documents)

  }, [documents])
  const handleDelete = async (record) => {
    const key = record.key
    const id = key.split('-')[0]
    console.log(record);
    
    const res = await axios.delete(`/api/v1/clusters/${id}/documents`, {
      data: [record.content]
    })

    if(res.data) {
      toast.success("EVENT DELETED SUCCESSFULLY")
    
      // Tạo một mảng mới không chứa record được truyền vào
      const newDocuments = filter.filter(child => child.key !== record.key);
    
      setFilter(newDocuments)

    }
  }
  return (
    <>
      {
        cluster &&
          <StyledDrawer width={750} placement="right" closable={false} onClose={onClose} open={open}>
            <p className='font-bold text-xl'>{title}</p>
            <div className='flex items-center gap-5'>
              <p className='!mb-0'>{author}</p>
              <time className='!mb-0' datetime={time}>{time}</time>
              {
                url &&
                <a href={url} target="_blank" className='btn btn--outline blue !mb-0'>Go to this news</a>
              }
            </div>
            <p className='font-bold'>
              {summary}
            </p>
            <p className='text-justify'>{content}</p>
          </StyledDrawer>
      }
      {
        !cluster &&
          <StyledDrawer width={1000} placement="right" closable={false} onClose={onClose} open={open}>
            <p className='font-bold text-xl'>{text}</p>
            <div className='flex items-center gap-5'>
              <time className='!mb-0' datetime={lastUpdated}>{lastUpdated}</time>
              {
                url &&
                <a href={url} target="_blank" className='btn btn--outline blue !mb-0'>Go to this news</a>
              }
            </div>
            <StyledTable
              columns={DOCUMENT_COLUMN_DEFS(handleDelete)}
              pagination={false}
              dataSource={filter}
              locale={{
                emptyText: <Empty text="No items found"/>
              }}
            />
          </StyledDrawer>
      }
    </>
  )
}

export default DrawerStyled