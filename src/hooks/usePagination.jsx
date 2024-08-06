import {useState, useEffect} from 'react';

const usePagination = (sizes, itemsPerPage = 10) => {
    const [currentPage, setCurrentPage] = useState(0);
    const maxPage = Math.ceil(sizes / itemsPerPage);

    // const currentItems = () => {
    //     const begin = (currentPage) * itemsPerPage;
    //     const end = begin + itemsPerPage;
    //     return data.slice(begin, end);
    // };

    useEffect(() => {
        if (currentPage > maxPage - 1) {
            setCurrentPage(maxPage - 1);
        } else if (currentPage < 0 && maxPage > 0) {
            setCurrentPage(0);
        }
    }, []);

    const next = () => {
        setCurrentPage((currentPage) => Math.min(currentPage + 1, maxPage));
    };

    const prev = () => {
        setCurrentPage((currentPage) => Math.max(currentPage - 1, 0));
    };

    const goToPage = (page) => {
        const pageNumber = Math.max(0, page);
        setCurrentPage(() => Math.min(pageNumber, maxPage));
    }

    const showingOf = () => {
        const begin = (currentPage) * itemsPerPage;
        const end = sizes > itemsPerPage ? begin + itemsPerPage : sizes;
        return sizes > 0 ?
            <>
                <span className="font-semibold">{end}</span>/{sizes}
            </>
            : '';
    }

    return {next, prev, goToPage, showingOf, currentPage, setCurrentPage, maxPage};
}

export default usePagination