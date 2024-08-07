import React, { useEffect, useState } from 'react'
import PageHeader from '../layouts/PageHeader'
import axios from 'axios'
import ClusterTable from '../widgets/ClusterTable'
import DB from '../db/cluster.json'
const Cluster = () => {
  
  const [isDisabled, setIsDisabled] = useState(false);
  const [clusterData, setClusterData] = useState({});
  const [isNoise, setIsNoise] = useState(false);
  const handleCrawl = async () => {
    console.log(DB);
    setIsDisabled(true)
    axios.defaults.baseURL = 'http://localhost:8081';
    const res = await axios.get('/api/v1/cluster/cluster-documents')
    setClusterData(res.data)
    // setClusterData(DB)
    setIsDisabled(false)
  }

  

  
  return (
    <>
        <PageHeader title="Clustering"/>
        <div className="flex flex-col flex-1 gap-5 md:gap-[26px]">
          <div className="w-full grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-[26px] lg:grid-cols-9 lg:items-end xl:grid-cols-12">
            <button disabled={isDisabled} className='btn btn--secondary blue !h-[44px] xl:col-span-12' onClick={handleCrawl}>Start Clustering</button>
          </div>
          <div className="w-full grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-[26px] lg:grid-cols-9 lg:items-end xl:grid-cols-12">
            <button disabled={!isNoise} onClick={() => setIsNoise(false)} className='xl:col-span-6 btn btn--outline green'>Active</button>
            <button disabled={isNoise} onClick={() => setIsNoise(true)} className='xl:col-span-6 btn btn--outline red'>Noise</button>
          </div>
          
          <ClusterTable isNoise={isNoise} data={clusterData} />
        </div>

    </>
  )
}

export default Cluster