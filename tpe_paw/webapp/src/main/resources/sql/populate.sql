/*
    TO manually populate database in terminal: psql -U postgres -d paw -a -f populate.sql
 */
delete from users where true;
delete from languages where true;
delete from tags where true;
delete from snippets where true;
delete from votes_for where true;
delete from favorites where true;
delete from follows where true;
delete from snippet_tags where true;

insert into users(username, password, email, description, reputation, date_joined) values('testUser','testPassword','testUser@gmail.com', 'I am the first user created on this web site', 3, timestamp '2019-09-28 12:30:00');

insert into languages(name) values('java');

insert into tags(name) values('sorting');
insert into tags(name) values('sort');
insert into tags(name) values('greedy');

insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='testUser'),'Implementation of merge sort','This is an implementation of merge sort in java',
'class MergeSort
{
    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    void merge(int arr[], int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int L[] = new int [n1];
        int R[] = new int [n2];

        /*Copy data to temp arrays*/
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];


        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2)
        {
            if (L[i] <= R[j])
            {
                arr[k] = L[i];
                i++;
            }
            else
            {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1)
        {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2)
        {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // Main function that sorts arr[l..r] using
    // merge()
    void sort(int arr[], int l, int r)
    {
        if (l < r)
        {
            // Find the middle point
            int m = (l+r)/2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr , m+1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    /* A utility function to print array of size n */
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i=0; i<n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }

    // Driver method
    public static void main(String args[])
    {
        int arr[] = {12, 11, 13, 5, 6, 7};

        System.out.println("Given Array");
        printArray(arr);

        MergeSort ob = new MergeSort();
        ob.sort(arr, 0, arr.length-1);

        System.out.println("\nSorted array");
        printArray(arr);
    }
} ', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='testUser'),'Snippet2','description','', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='testUser'),'Snippet3','description','javajavjajavjajvajvjajvjavjajvja', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));
insert into snippets(user_id, title, description, code, date_created, language_id) values((select id from users where username='testUser'),'Snippet4','description','javajavjajavjajvajvjajvjavjajvja', timestamp '2019-09-28 12:40:00', (select id from languages where name='java'));

insert into votes_for(user_id, snippet_id) values((select id from users where username='testUser'),(select id from snippets where title='Implementation of merge sort'));

insert into favorites(snippet_id,user_id) values((select id from snippets where title='Snippet3'),(select id from users where username='testUser'));

insert into follows(user_id,tag_id) values((select id from users where username='testUser'),(select id from tags where name='sort'));

insert into snippet_tags(snippet_id,tag_id) values((select id from snippets where title='Snippet4'),(select id from tags where name='greedy'));

insert into users values(default, 'igrib', 'pass', 'igrib@gmail.com', 'desc1', 35, timestamp '2019-09-28 12:40:00', null);
insert into users values(default, 'igrib2', 'pass', 'igrib2@gmail.com', 'desc2', 35, timestamp '2019-09-28 12:40:00', null);
insert into snippets values(default, (select id from users where email = 'igrib@gmail.com'), 'Some HTML Code', 'This code is about testing something', '<html>HOLA</html>',  timestamp '2019-09-28 12:40:00');
insert into snippets values(default, (select id from users where email = 'igrib2@gmail.com'), 'Some HTML Code', 'This code is about testing something', '<html>CHAU</html>',  timestamp '2019-09-28 12:40:00');