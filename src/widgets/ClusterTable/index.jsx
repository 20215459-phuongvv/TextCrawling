import React, { useEffect, useState } from 'react'
import StyledTable from './styles'
import { CLUSTER_COLUMN_DEFS } from '../../constants/columnDefs'
import Empty from '../../components/Empty'
import usePagination from '../../hooks/usePagination'
import Pagination from '../../ui/Pagination'
import axios from 'axios'

const ClusterTable = ({ data , isNoise }) => {
  const {
    num_clusters = 0, 
    num_clustered_documents = 0, 
    num_noise_documents = 0, 
    clusters = [], 
    noise_documents = []
  } = data
  const [filterData, setFilterData] = useState([]);
  const pagination = usePagination(isNoise ? num_noise_documents : num_clustered_documents);
  console.log(pagination);
  
  const flatData = (array) => {
    return array.flatMap(arr => arr.documents);
  }

  const itemsPerPage = 10;
  const begin = (pagination.currentPage) * itemsPerPage;
  const end = begin + itemsPerPage;

  useEffect(() => {
    if (isNoise) {
      setFilterData(noise_documents);
    } else {
      setFilterData(flatData(clusters));
    }    
    
  }, [isNoise, data])

  useEffect(() => {
    pagination.goToPage(0);
  }, [pagination.maxPage, isNoise])

  return (
    <>
      <div>
        <StyledTable
            columns={CLUSTER_COLUMN_DEFS}
            pagination={false}
            dataSource={filterData.slice(begin, end)}
            locale={{
              emptyText: <Empty text="No items found"/>
            }}
            rowKey={(record) => (record.text)}
        />
      </div>
      {
        pagination.maxPage > 0 && <Pagination pagination={pagination} />
      }
    </>
  )
}

export default ClusterTable