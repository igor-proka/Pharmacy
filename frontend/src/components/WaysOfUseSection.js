// src/components/WaysOfUseSection.js
import React, { useEffect, useState } from 'react';
import './Section.css';

function WaysOfUseSection() {
  const [data, setData] = useState([]);
  const [newDescription, setNewDescription] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);
    fetch('/api/ways-of-use')
      .then((res) => res.json())
      .then(setData)
      .catch(console.error)
      .finally(() => setIsLoading(false));
  }, []);

  const handleAdd = () => {
    if (!newDescription.trim()) {
      alert('Описание не может быть пустым.');
      return;
    }

    if (data.some(item => item.description.toLowerCase() === newDescription.trim().toLowerCase())) {
      alert('Такое описание уже существует.');
      return;
    }

    fetch('/api/ways-of-use', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ description: newDescription }),
    })
      .then(async (res) => {
        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(errorText || 'Ошибка добавления');
        }
        return res.json();
      })
      .then((created) => {
        setData([...data, created]);
        setNewDescription('');
      })
      .catch((error) => {
        alert('Ошибка при добавлении: ' + error.message);
      });
  };

  const handleDelete = (id) => {
    if (!window.confirm('Вы уверены, что хотите удалить этот элемент?')) return;

    fetch(`/api/ways-of-use/${id}`, {
      method: 'DELETE',
    }).then(() => {
      setData(data.filter((item) => item.id !== id));
    });
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setNewDescription(item.description);
  };

  const handleUpdate = () => {
    if (!newDescription.trim()) {
      alert('Описание не может быть пустым.');
      return;
    }

    if (
      data.some(
        (item) =>
          item.id !== editingItem.id &&
          item.description.toLowerCase() === newDescription.trim().toLowerCase()
      )
    ) {
      alert('Такое описание уже существует.');
      return;
    }

    fetch(`/api/ways-of-use/${editingItem.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ description: newDescription }),
    })
      .then(async (res) => {
        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(errorText || 'Ошибка при обновлении');
        }
        return res.json();
      })
      .then((updated) => {
        setData(data.map((item) => (item.id === updated.id ? updated : item)));
        setEditingItem(null);
        setNewDescription('');
      })
      .catch((error) => {
        alert('Ошибка при обновлении: ' + error.message);
      });
  };

  const handleCancel = () => {
    setEditingItem(null);
    setNewDescription('');
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Способы использования</h2>

      <div className="form-container">
        <input
          type="text"
          value={newDescription}
          onChange={(e) => setNewDescription(e.target.value)}
          placeholder="Введите описание..."
          className="form-input"
        />
        <div className="button-group">
          {editingItem ? (
            <>
              <button className="btn btn-save" onClick={handleUpdate}>
                Сохранить
              </button>
              <button className="btn btn-cancel" onClick={handleCancel}>
                Отмена
              </button>
            </>
          ) : (
            <button className="btn btn-add" onClick={handleAdd}>
              Добавить
            </button>
          )}
        </div>
      </div>

      {isLoading ? (
        <div className="loading-spinner"></div>
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Описание</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {data.length > 0 ? (
                data.map((item) => (
                  <tr key={item.id}>
                    <td>{item.description}</td>
                    <td className="actions-cell">
                      <button
                        className="btn btn-edit"
                        onClick={() => handleEdit(item)}
                      >
                        Редактировать
                      </button>
                      <button
                        className="btn btn-delete"
                        onClick={() => handleDelete(item.id)}
                      >
                        Удалить
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="2" className="no-data">
                    Нет данных для отображения
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default WaysOfUseSection;
