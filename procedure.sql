IF OBJECT_ID('SPDeleteOrder') IS NOT NULL
    DROP PROC SPDeleteOrder;
GO
CREATE PROC SPDeleteOrder(@Id bigint)
AS
BEGIN TRY
BEGIN TRAN;
DELETE tbl_order_item
WHERE id IN
      (
          SELECT id FROM tbl_order_item WHERE @Id = order_id
      );
DELETE tbl_order
WHERE id IN
      (
          SELECT id FROM tbl_order WHERE @Id = id and (status = 'CREATED' or status = 'CANCELLED')
      );
COMMIT TRAN;
END TRY
BEGIN CATCH
ROLLBACK TRANSACTION;
END CATCH;