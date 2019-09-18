package function

import (
	"database/sql"
	"fmt"
	_ "gopkg.in/goracle.v2"
	"net/http"
)

func OracleConnection(w http.ResponseWriter, r *http.Request) {

	/*	dbIp := os.Getenv("ORACLE_IP")
		dbSchema := os.Getenv("ORACLE_SCHEMA")
		dbUser := os.Getenv("ORACLE_USER")
		dbPassword := os.Getenv("ORACLE_PASSWORD")

		db, err := sql.Open("goracle", dbUser + "/" + dbPassword + "@" + dbIp + ":1521/" + dbSchema)
	*/db, err := sql.Open("goracle", "system/root@35.222.1.145:1521/xe")
	if err != nil {
		fmt.Println(err)
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintln(w, err)
		return
	}
	defer db.Close()

	rows, err := db.Query("select 'Great!' from dual")
	if err != nil {
		fmt.Println("Error running query")
		fmt.Println(err)
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Fprintln(w, err)
		return
	}
	defer rows.Close()

	var value string
	for rows.Next() {
		rows.Scan(&value)
	}
	w.WriteHeader(http.StatusOK)
	fmt.Fprintln(w, value)
}
