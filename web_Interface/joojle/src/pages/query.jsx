//query page

import { useState, useEffect } from "react";
import SearchResult from "@/components/SearchResult";

function query() {
    const [q, setQ] = useState();
    const [result, setResult] = useState();

    useEffect(() => {
        async function getResults(q) {
            const res = await fetch(
                "https://jsonplaceholder.typicode.com/comments"
            );
            const json = await res.json();
            var mapped_items = await json.map((item) => {
                return <SearchResult key={item.id} id={item.id} />;
            });
            await setResult(mapped_items);
        }

        getResults(new URLSearchParams(window.location.search).get("q"));
    }, [result]);

    return <h1> {result ? result : "loading"}</h1>;
}
export default query;
