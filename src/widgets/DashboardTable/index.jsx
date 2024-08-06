import React, { useEffect, useState } from 'react'
import StyledTable from './styles'
import DrawerStyler from '../../ui/Drawer'
import { DASHBOARD_COLUMN_DEFS } from '../../constants/columnDefs'
import Empty from '../../components/Empty'
import usePagination from '../../hooks/usePagination'
import Pagination from '../../ui/Pagination'
import axios from 'axios'
const DashboardTable = ({date, title, tag}) => {
  const [filterData, setFilterData] = useState([]);
  const [open, setOpen] = useState(false);
  const pagination = usePagination(filterData?.total);
  const [initData, setInitData] = useState('');
  const fetchData = async () => {
    let from = '', to = ''
    if(date) {
      from = date[0].format('YYYY-MM-DDTHH:mm:ss')
      to = date[1].format('YYYY-MM-DDTHH:mm:ss')
    }
    const res = await axios.get(`/api/v1/posts?from=${from}&to=${to}&title=${title}&tagList=${tag}&page=${pagination.currentPage}`);
    setFilterData(res?.data)
  }

  const handleClick = (record) => {
    setOpen(true);
    setInitData(record);
    console.log(record);
  }

  useEffect(() => {
    
    fetchData()

  }, [date, title, tag, pagination.currentPage])
  return (
    <>
      <div>
        <StyledTable
            columns={DASHBOARD_COLUMN_DEFS(handleClick)}
            pagination={false}
            dataSource={filterData?.postEntityList}
            locale={{
              emptyText: <Empty text="No items found"/>
            }}
            rowKey={(record) => (record.url)}
        />
      </div>
      {
        pagination.maxPage > 0 && <Pagination pagination={pagination} />
      }
      <DrawerStyler data={initData} open={open} onClose={() => setOpen(false)} />
    </>
  )
}

export default DashboardTable