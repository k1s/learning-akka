from subprocess import call

nums = [78, 54, 39, 43, 55, 145, 68, 40, 41, 42]


ids = [str(x) for x in nums]

query = """START TRANSACTION;

CREATE TABLE delete_ids (
    ID INT NOT NULL PRIMARY KEY
);

INSERT INTO delete_ids (id)
SELECT line.id
FROM pricelist p
LEFT JOIN (
    SELECT p.id
FROM pricelist p
WHERE supplier_id = supplierId
ORDER BY id DESC
LIMIT 15) AS last15
ON last15.id = p.id
JOIN pricelist_line line ON line.pricelist_id = p.id
WHERE p.supplier_id = supplierId AND last15.id IS NULL;


DELETE FROM pricelist_line
WHERE id IN (SELECT id
FROM delete_ids);

DROP TABLE delete_ids;

COMMIT;"""

queries = [query.replace("supplierId", x) for x in ids]

[call(["mysql", "-u", "root", "-psystem001", "-h", "cab-map", "arm", "-e", q]) for q in queries]
