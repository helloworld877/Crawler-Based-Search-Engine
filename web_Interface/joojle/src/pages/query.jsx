//query page

import { useState, useEffect } from "react";
import SearchResult from "@/components/SearchResult";
import styles from "../styles/Query.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";

function query() {
    const [q, setQ] = useState();
    const [result, setResult] = useState();
    const [query, setQuery] = useState();
    // handle change of query when typing
    const handleChange = (event) => {
        setQuery(event.target.value);
    };

    useEffect(() => {
        async function getResults(q) {
            const res = await fetch(`http://localhost:8080/query?q=${q}`);
            const json = await res.json();
            var mapped_items = await json.map((item) => {
                return <SearchResult key={item.id} res={item} />;
            });

            if (mapped_items.length === 0) {
                mapped_items = <h2>No Results 😭😢</h2>;
            }
            await setResult(mapped_items);
        }

        getResults(new URLSearchParams(window.location.search).get("q"));
    }, []);

    return (
        <div>
            <div className={`container-fluid ${styles.resultsContainerTotal}`}>
                <div className="row">
                    <div className={styles.form}>
                        <FontAwesomeIcon
                            icon={faMagnifyingGlass}
                            className={styles.faSearch}
                        />
                        <input
                            type="text"
                            className={`${styles.formControl} ${styles.formInput}`}
                            placeholder="Search anything..."
                            onChange={handleChange}
                        />
                    </div>
                </div>
                <div className={`row ${styles.resultItems}`}>
                    <div className="col">
                        {result ? result : <h2>Loading⌚</h2>}
                    </div>
                </div>
            </div>
        </div>
    );
}
export default query;
