import classNames from 'classnames';

const Pagination = ({pagination}) => {
    const total = pagination.maxPage;
    const NUMOFPAGE = Math.min(total, 10)
    return (
        <div className="flex flex-wrap items-center gap-[18px] pb-6 border-b border-input-border">
            {
                pagination.currentPage > 0 &&
                <button onClick={pagination.prev} aria-label="Previous page">
                    <i className="icon icon-chevrons-left-solid"/>
                </button>
            }
            <div className="flex flex-wrap gap-2.5">
                {
                    pagination.currentPage < 10 && [...Array(NUMOFPAGE)].map((_, i) => {
                        return (
                            <button
                                className={classNames('page-btn subheading-2', {'active': i === pagination.currentPage})}
                                key={i}
                                onClick={() => pagination.goToPage(i)}
                                disabled={pagination.currentPage === i}
                                aria-label={`Page ${i + 1}`}>
                                {i + 1}
                            </button>
                        );
                    })
                }
                {
                    pagination.currentPage >= 10 && Array.from({ length: NUMOFPAGE }, (_, index) => pagination.currentPage - 9 + index).map((_, i) => {
                        return (
                            <button
                                className={classNames('page-btn subheading-2', {'active': _ === pagination.currentPage})}
                                key={_}
                                onClick={() => pagination.goToPage(_)}
                                disabled={pagination.currentPage === _}
                                aria-label={`Page ${_ + 1}`}>
                                {_ + 1}
                            </button>
                        );
                    })
                }
            </div>
            {
                pagination.currentPage < total - 1 &&
                <button onClick={pagination.next}
                        disabled={pagination.currentPage + 1 === pagination.maxPage}
                        aria-label="Next page">
                    <i className="icon icon-chevrons-right-solid"/>
                </button>
            }
        </div>
    )
}

export default Pagination