//query page

import { useState, useEffect } from "react";
import SearchResult from "@/components/SearchResult";
import axios from "axios";

function query() {
    const [q, setQ] = useState();
    const [result, setResult] = useState();

    useEffect(() => {
        async function getResults(q) {
            const res = await fetch("http://localhost:8080/query?q=test");
            const json = await res.json();
            var mapped_items = await json.map((item) => {
                return <SearchResult key={item.id} id={item.body} />;
            });
            await setResult(mapped_items);
        }

        getResults(new URLSearchParams(window.location.search).get("q"));
    }, [result]);

    return <h1> {result ? result : "loading"}</h1>;
}
export default query;
