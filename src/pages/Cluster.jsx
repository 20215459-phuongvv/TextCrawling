import React, { useEffect, useState } from 'react'
import PageHeader from '../layouts/PageHeader'
import axios from 'axios'
import ClusterTable from '../widgets/ClusterTable'
import DB from '../db/cluster.json'
const Cluster = () => {
  
  const [clusterData, setClusterData] = useState({});
  const [isNoise, setIsNoise] = useState(false);


  return (
    <>
        <PageHeader title="Clustering"/>
        <div className="flex flex-col flex-1 gap-5 md:gap-[26px]">
          <ClusterTable isNoise={isNoise} data={clusterData} />
        </div>

    </>
  )
}

export default Cluster