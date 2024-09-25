import React from 'react'
import { Helmet } from 'react-helmet'

const DocumentTitle = ({title}) => {
    return (
        <Helmet>
            <title>{title} | Persimmon</title>
        </Helmet>
    )
}

export default DocumentTitle