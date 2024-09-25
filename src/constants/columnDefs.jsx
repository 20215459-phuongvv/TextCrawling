import Timestamp from "../ui/Timestamp";
import SubmenuTrigger from "../ui/SubmenuTrigger";
import TruncatedText from "../components/TruncatedText";
import { NavLink } from "react-router-dom";
import trash from "../assets/trash.svg";
export const DASHBOARD_COLUMN_DEFS = (onClick) => [
  {
    key: 1,
    title: "City",
    dataIndex: "City",
    width: "10%",
    render: (title) => (
      <TruncatedText
        className="font-heading font-bold"
        lines={3}
        text={title}
      />
    ),
  },
  {
    key: 2,
    title: "Product Type",
    dataIndex: "Product Type",
    // responsive: ['lg'],
    width: "10%",
    render: (title) => (
      <TruncatedText
        className="font-heading font-bold"
        lines={3}
        text={title}
      />
    ),
  },
  {
    key: 3,
    title: "Trip or Order Status",
    dataIndex: "Trip or Order Status",
    responsive: ["xl"],
    width: "10%",
    render: (title) => (
      <TruncatedText
        className="font-heading font-bold"
        lines={3}
        text={title}
      />
    ),
  },
  {
    key: 4,
    title: "Request Time",
    dataIndex: "Request Time",
    responsive: ["xxl"],
    width: "10%",
    render: (title) => (
      <TruncatedText
        className="font-heading font-bold"
        lines={3}
        text={title}
      />
    ),
  },
  {
    key: 5,
    title: "Begin Trip Time",
    dataIndex: "Begin Trip Time",
    responsive: ["xl"],
    width: "10%",
    render: (title) => (
        <TruncatedText
          className="font-heading font-bold"
          lines={3}
          text={title}
        />
      ),
  },
  {
    key: 6,
    title: "Actions",
    dataIndex: "actions",
    width: "5%",
    render: (_, record) => (
      <div className="flex items-center justify-end gap-11">
        <NavLink aria-label="Edit" onClick={() => onClick(record)}>
          <i className="icon icon-pen-to-square-regular text-lg leading-none" />
        </NavLink>
        {/* <SubmenuTrigger/> */}
      </div>
    ),
  },
];

export const CLUSTER_COLUMN_DEFS = (onEdit, onDelete) => [
  {
    key: 2,
    title: "Title",
    dataIndex: "text",
    render: (text) => (
      <TruncatedText className="font-heading font-bold" lines={3} text={text} />
    ),
  },
  {
    key: 1,
    title: "Date & Time",
    dataIndex: "time",
    width: "10%",
    render: (time) => <Timestamp date={time} />,
  },
  {
    key: 6,
    title: "",
    dataIndex: "actions",
    width: "5%",
    render: (_, record) => (
      <div className="flex items-center justify-end gap-1">
        <NavLink aria-label="Edit">
          <i
            className="icon icon-pen-to-square-regular text-lg leading-none"
            onClick={() => onEdit(record)}
          />
        </NavLink>
        <NavLink aria-label="Delete">
          <img src={trash} alt="" onClick={() => onDelete(record)} />
        </NavLink>
      </div>
    ),
  },
];

export const DOCUMENT_COLUMN_DEFS = (onDelete) => [
  {
    key: 2,
    title: "Title",
    dataIndex: "text",
    render: (text) => (
      <TruncatedText className="font-heading font-bold" lines={3} text={text} />
    ),
  },
  {
    key: 1,
    title: "Date & Time",
    dataIndex: "time",
    width: "10%",
    render: (time) => <Timestamp date={time} />,
  },
  {
    key: 6,
    title: "",
    dataIndex: "actions",
    width: "5%",
    render: (_, record) => (
      <div className="flex items-center justify-end gap-1">
        <NavLink aria-label="Delete">
          <img src={trash} alt="" onClick={() => onDelete(record)} />
        </NavLink>
      </div>
    ),
  },
];
