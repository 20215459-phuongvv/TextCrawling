import Timestamp from '../ui/Timestamp';
import SubmenuTrigger from '../ui/SubmenuTrigger';
import TruncatedText from '../components/TruncatedText';
import { NavLink } from 'react-router-dom';
import trash from '../assets/trash.svg';
export const DASHBOARD_COLUMN_DEFS = (onClick) => [
    {
        key: 1,
        title: 'Date & Time',
        dataIndex: 'time',
        width: '10%',
        render: timestamp => <Timestamp date={timestamp}/>,
    },
    {
        key: 2,
        title: 'Title',
        dataIndex: 'title',
        // responsive: ['lg'],  
        width: '20%',
        render: title => <TruncatedText className="font-heading font-bold" lines={3} text={title} />
    },
    {
        key: 3,
        title: 'Summary',
        dataIndex: 'summary',
        responsive: ['xl'],
        width: '40%',
        render: sumary => <div className="flex flex-1 gap-5 bg-input-bg border border-input-border h-20 rounded-md
                            max-w-[500px] p-4 overflow-hidden">
                            <div className="flex-1 max-w-[513px]">
                                <TruncatedText className="flex-1" text={sumary} width={'513px'} />
                            </div>
                        </div>
    },
    {
        key: 4,
        title: 'Source',
        dataIndex: 'source',
        responsive: ['xxl'],
        width: '10%',
        render: source => <span className='text-accent capitalize'>{source}</span>
    },
    {
        key: 5,
        title: 'tags',
        dataIndex: 'tagList',
        responsive: ['xl'],
        width: '10%',
        render: tagList => tagList?.map(tag => <span key={tag} className="capitalize">{tag}, </span>)
    },
    {
        key: 6,
        title: 'Actions',
        dataIndex: 'actions',
        width: '5%',
        render: (_, record) =>
            <div className="flex items-center justify-end gap-11">
                <NavLink aria-label="Edit" onClick={() => onClick(record)}>
                    <i className="icon icon-pen-to-square-regular text-lg leading-none"/>
                </NavLink>
                {/* <SubmenuTrigger/> */}
            </div>
    }
];

export const CLUSTER_COLUMN_DEFS = (onEdit, onDelete) => [
    {
        key: 2,
        title: 'Title',
        dataIndex: 'text',
        render: text => <TruncatedText className="font-heading font-bold" lines={3} text={text} />
    },
    {
        key: 1,
        title: 'Date & Time',
        dataIndex: 'lastUpdated',
        width: '10%',
        render: lastUpdated => <Timestamp date={lastUpdated}/>,
    },
    {
        key: 6,
        title: '',
        dataIndex: 'actions',
        width: '5%',
        render: (_, record) =>
            <div className="flex items-center justify-end gap-1">
                <NavLink aria-label="Edit">
                    <i className="icon icon-pen-to-square-regular text-lg leading-none" onClick={() => onEdit(record)}/>
                </NavLink>
                <NavLink aria-label="Delete">
                    <img src={trash} alt="" onClick={() => onDelete(record)} />
                </NavLink>
            </div>
    }
];

export const DOCUMENT_COLUMN_DEFS = (onDelete) => [
    {
        key: 2,
        title: 'Title',
        dataIndex: 'text',
        render: text => <TruncatedText className="font-heading font-bold" lines={3} text={text} />
    },
    {
        key: 1,
        title: 'Date & Time',
        dataIndex: 'time',
        width: '10%',
        render: time => <Timestamp date={time}/>,
    },
    {
        key: 6,
        title: '',
        dataIndex: 'actions',
        width: '5%',
        render: (_, record) =>
            <div className="flex items-center justify-end gap-1">
                <NavLink aria-label="Delete">
                    <img src={trash} alt="" onClick={() => onDelete(record)} />
                </NavLink>
            </div>
    }
];