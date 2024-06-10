SELECT [Sector], SUM([B/w]) as 'B/W vs Prior Year By Sector' FROM spend_data WHERE 1=1 --$conditions$
GROUP BY [Sector]