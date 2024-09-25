import React, { useEffect, useState } from "react";
import StyledTable from "./styles";
import DrawerStyler from "../../ui/Drawer";
import { DASHBOARD_COLUMN_DEFS } from "../../constants/columnDefs";
import Empty from "../../components/Empty";
import usePagination from "../../hooks/usePagination";
import Pagination from "../../ui/Pagination";
import axios from "axios";
import Papa from "papaparse";

const DashboardTable = ({ date, title, tag, isCrawlActive }) => {
  const [filterData, setFilterData] = useState([]);
  const [open, setOpen] = useState(false);
  const pagination = usePagination(filterData?.total);
  const [initData, setInitData] = useState("");
  const fetchData = async () => {
    let from = "",
      to = "";
    if (date) {
      from = date[0].format("YYYY-MM-DDTHH:mm:ss");
      to = date[1].format("YYYY-MM-DDTHH:mm:ss");
    }
    // const res = await axios.get(`/api/v1/posts?from=${from}&to=${to}&title=${title}&tagList=${tag}&page=${pagination.currentPage}`);
    // setFilterData(res?.data)
    setTimeout(async () => {
      fetch("./src/db/data.csv")
        .then(response => response.text())
        .then(csvText => {
          Papa.parse(csvText, {
            header: true,
            dynamicTyping: true,
            complete: (results) => {
              console.log("Parsed results:", results.data);
              setFilterData({
                postEntityList: results.data,
                total: results.data.length,
              });
            },
          });
        });
    }, 10000);
  };

  const handleClick = (record) => {
    setOpen(true);
    setInitData(record);
    console.log(record);
  };

  useEffect(() => {
    if(isCrawlActive) {
      fetchData();
    }  
  }, [date, title, tag, isCrawlActive, pagination.currentPage]);
  return (
    <>
      <div>
        <StyledTable
          columns={DASHBOARD_COLUMN_DEFS(handleClick)}
          pagination={false}
          dataSource={filterData?.postEntityList}
          locale={{
            emptyText: <Empty text="No items found" />,
          }}
          rowKey={(record) => record.RequestTime + record.BeginTripLat}
        />
      </div>
      {pagination.maxPage > 0 && <Pagination pagination={pagination} />}
      <DrawerStyler
        data={initData}
        open={open}
        onClose={() => setOpen(false)}
      />
    </>
  );
};

export default DashboardTable;
