//query page

import { useState, useEffect } from "react";

function query() {
    const [q, setQ] = useState();
    useEffect(() => {
        setQ(new URLSearchParams(window.location.search).get("q"));
    });

    return <h1> {q}</h1>;
}
export default query;
