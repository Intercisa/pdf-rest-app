import React, { useState, useEffect } from 'react';
import FileType from '../models/FileType';
import ListItem from '../models/ListItem'
import DownloadableItem from './DownloadableItem'

interface ItemListProps {
  apiUrl: string; // URL of the backend API
  refresh: boolean;
  fileType: FileType
}

function setListTitle(fileType: FileType) {
    if (fileType === FileType.PDF) {
      return "PDF List"
    } 
    return "Picture List"
}

const FileList: React.FC<ItemListProps> = ( props ) => {
  // State to hold the list of items
  const [items, setItems] = useState<ListItem[]>([]);

  // Fetch items from the backend API when the component mounts
  useEffect(() => {
    fetchItems();
  }, [props.refresh]);

  const listTile = setListTitle(props.fileType)
  // Function to fetch items from the backend API
  const fetchItems = async () => {
    try {
      const response = await fetch(props.apiUrl);
      if (!response.ok) {
        throw new Error('Failed to fetch items');
      }
      const data = await response.json();
      setItems(data);
    } catch (error) {
      console.error('Error fetching items:', error);
    }



  };

  return (
    <div>
      <h2>
       {listTile} 
      </h2>
      <ul>
        {items.map((item) => (
             <li key={item.id}>
             <DownloadableItem id={item.id} name={item.name} size={item.size} url={item.url} />
           </li>
        ))}
      </ul>
    </div>
  );
};

export default FileList;
